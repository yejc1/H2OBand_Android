package seniordesign.h2oband_03;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by saumil on 4/16/17.
 */

public class MainService extends Service {
    public static final String ACTION_UPDATE_DRAIN_VELOCITY = "update_d_vel";
    public static final String ACTION_UPDATE_NOTIFICATION_INT = "update_not_int";
    public static final String ACTION_REQUEST_INFO = "req_info";
    public static final String ACTION_INFO_UPDATE = "info_update";
    public static final String INTENT_DRAIN_VELOCITY = "d_vel";
    public static final String INTENT_PERCENT_FULL = "per_full";
    public static final String INTENT_NOTIF_INT = "not_int";
    public static final String INTENT_GOAL_30_SEC = "goal30sec";


    /* **************** Bottle Info **************** */
    private class H2OBand_Info {
        private final int BOTTLE_MAX = 100;

        private int mDrainVelocity;
        private int mPercentFull;
        private int mNotificationIntervalSeconds;
        private int mGoal30Sec;

        private int mPercentFullLastCheckpoint;
        private long mLastCheckpoint;

        public H2OBand_Info() {
            /* Initial bottle settings */
            mDrainVelocity = 0;
            mPercentFull = BOTTLE_MAX;
            mNotificationIntervalSeconds = 15;
            mGoal30Sec = 5;

            mPercentFullLastCheckpoint = BOTTLE_MAX;
            mLastCheckpoint = System.currentTimeMillis();
        }

        /**
         * Increments or decrements the stored information about the bottle
         */
        void updatePercentFull() {
            if(mPercentFull == 0)
                return;

            mPercentFull -= mDrainVelocity;
            if(mPercentFull < 0) {
                mPercentFull = 0;
            }
        }

        /**
         * Determines whether the goal for the amount of water to drink has been satisfied
         * @return  True if the goal has been achieved
     *   *          False otherwise
         */
        boolean goalAchieved() {
            int goal = mNotificationIntervalSeconds * mGoal30Sec / 30;
            return mPercentFullLastCheckpoint - mPercentFull >= goal;
        }

        /**
         * Determines whether the system has waited the required time interval betewen notficiations
         * @return  True if enough time has passed between last checkpoint and current time
         *          False otherwise
         */
        boolean notificationTimeIntervalAchieved() {
            if((System.currentTimeMillis() - mLastCheckpoint) / 1000 >=
                    mNotificationIntervalSeconds) {
                mLastCheckpoint = System.currentTimeMillis();
                return true;
            }
            return false;
        }




        /* **************** Setters and getters **************** */

        void setDrainVelocity(int drainVelocity) {
            mDrainVelocity = drainVelocity;
        }

        void setPercentFull(int percentFull) {
            mPercentFull = percentFull;
        }

        void setNotificationIntervalSeconds(int notificationIntervalSeconds) {
            mNotificationIntervalSeconds = notificationIntervalSeconds;
        }

        void setGoal30Sec(int goal) {
            mGoal30Sec = goal;
        }

        void setLastCheckpoint(long checkpoint) {
            mLastCheckpoint = checkpoint;
        }

        void setPercentFullLastCheckpoint(int percentFullLastCheckpoint) {
            mPercentFullLastCheckpoint = percentFullLastCheckpoint;
        }

        int getDrainVelocity() {
            return mDrainVelocity;
        }

        int getPercentFull() {
            return mPercentFull;
        }

        int getNotificationIntervalSeconds() {
            return mNotificationIntervalSeconds;
        }

        int getGoal30Sec() {
            return mGoal30Sec;
        }

        long getLastCheckpoint() {
            return mLastCheckpoint;
        }

        int getPercentFullLastCheckpoint() {
            return mPercentFullLastCheckpoint;
        }
    }
    H2OBand_Info info;




    /* **************** System **************** */
    private final H2OBandUpdateThread h2oThread = new H2OBandUpdateThread();
    NotificationCompat.Builder nBuilder;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_UPDATE_NOTIFICATION_INT)) {
                info.setNotificationIntervalSeconds(intent.getExtras().getInt(INTENT_NOTIF_INT));
            } else if(intent.getAction().equals(ACTION_REQUEST_INFO)) {
                Intent response = new Intent(ACTION_INFO_UPDATE);
                response.putExtra(INTENT_DRAIN_VELOCITY, info.getDrainVelocity());
                response.putExtra(INTENT_PERCENT_FULL, info.getPercentFull());
                response.putExtra(INTENT_NOTIF_INT, info.getNotificationIntervalSeconds());
                response.putExtra(INTENT_GOAL_30_SEC, info.getGoal30Sec());
                sendBroadcast(intent);
            }
        }
    };







    /* **************** Implementation **************** */

    @Override
    public void onCreate() {
        super.onCreate();
        nBuilder = new NotificationCompat.Builder(this);

        info = new H2OBand_Info();

        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION_INT));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //monitorThread.start();
        if(!h2oThread.isAlive() || h2oThread.isInterrupted())
            h2oThread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(h2oThread.isAlive() && !h2oThread.isInterrupted())
            h2oThread.interrupt();

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }













    private class H2OBandUpdateThread extends Thread implements Runnable {
        /* **************** Server settings **************** */
        private final int PORT = 8080;
        private final int BACKLOG = 5;
        private final int TIMEOUT = 100;
        private ServerSocket mServerSocket;


        /**
         * Configures the server socket and initializes the initial bottle properties
         */
        public H2OBandUpdateThread() {
            /* Configure server settings */
            try {
                mServerSocket = new ServerSocket(PORT, BACKLOG);
                mServerSocket.setSoTimeout(TIMEOUT);
            } catch(IOException e) {
                mServerSocket = null;
            }
        }

        @Override
        public void run() {
            while(!Thread.interrupted()) {
                if (monitorSocket()) {
                    Intent intent = new Intent(ACTION_UPDATE_DRAIN_VELOCITY);
                    intent.putExtra(INTENT_DRAIN_VELOCITY, info.getDrainVelocity());
                    sendBroadcast(intent);
                }
                checkNotification();
            }
        }





        /**
         * Checks the socket to see whether a request to change the drain velocity has arrived
         * @return  True if drain velocity has been changed
         *          False otherwise
         */
        private boolean monitorSocket() {
            if(mServerSocket == null)
                return false;

            final String response = "Received";

            try {
                Socket socket = mServerSocket.accept();
                Log.d("MonitorThread", "Received connection");

                byte counter = 0;
                int drainVelocity = 0;
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                for (byte c = (byte) input.read(); c != 0; c = (byte) input.read())
                    drainVelocity += (c << (8 * counter++));
                info.setDrainVelocity(drainVelocity);

                Log.d("MonitorThread", "mDrainVelocity = " + drainVelocity);

                OutputStream output = socket.getOutputStream();
                output.write(response.getBytes());

                socket.close();

                return true;
            } catch(IOException e) {
                return false;
            }
        }

        /**
         * Determines whether a notification needs to be sent out depending on the time interval
         * specified by the user and whether or not the user has fulfilled the fluid intake
         * requirement
         */
        private void checkNotification() {
            if(info.notificationTimeIntervalAchieved()) {
                Log.d("MainService", "Checking if goal is achieved");
                if(!info.goalAchieved()) {
                    Log.d("MainService", "Goal is not achieved");
                    sendNotification();
                }
            }
        }

        /**
         * Sends a notification reminding the user to drink more water
         */
        private void sendNotification() {
            nBuilder.setSmallIcon(R.drawable.bottle);
            nBuilder.setTicker("You need to drink more water");
            nBuilder.setWhen(System.currentTimeMillis());
            nBuilder.setContentTitle("H2OBand");
            nBuilder.setContentText("You need to drink more water");

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(getApplicationContext(), 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            nBuilder.setContentIntent(pendingIntent);
            NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nManager.notify(20, nBuilder.build());
        }
    }



















    private class MonitorThread extends Thread implements Runnable {
        private final int PORT = 8000;
        private final int BACKLOG = 5;
        private final int TIMEOUT = 100;

        @Override
        public void run() {
            String response = "Received";
            ServerSocket serverSocket;
            Socket socket;

            try {
                serverSocket = new ServerSocket(PORT, BACKLOG);
                serverSocket.setSoTimeout(TIMEOUT);
            } catch(IOException e) {
                e.printStackTrace();
                return;
            }

            while(!Thread.interrupted()) {
                try {
                    socket = serverSocket.accept();
                    Log.d("MonitorThread", "Received connection");

                    Intent intent = new Intent("update");

                    String result = "";
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    for(char c = (char)input.read(); c != 0; c = (char)input.read())
                        result += c;
                    Log.d("MonitorThread", result);

                    OutputStream output = socket.getOutputStream();
                    output.write(response.getBytes());

                    socket.close();
                } catch(SocketTimeoutException e) {
                } catch(Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

            try {
                serverSocket.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
