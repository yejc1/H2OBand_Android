package seniordesign.h2oband_03;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by saumil on 4/16/17.
 */

public class MainService extends Service {
    // Intent actions
    public static final String ACTION_UPDATE_DRAIN_VELOCITY = "update_d_vel";
    public static final String ACTION_UPDATE_NOTIFICATION_INT = "update_not_int";
    public static final String ACTION_UPDATE_GOAL = "update_goal";
    public static final String ACTION_REQUEST_INFO = "req_info";
    public static final String ACTION_INFO_UPDATE = "info_update";
    public static final String ACTION_UPDATE_FROM_TIME = "update_from_time";
    public static final String ACTION_UPDATE_TO_TIME = "update_to_time";
    public static final String ACTION_UPDATE_WEIGHT = "update_weight";
    public static final String ACTION_UPDATE_ACTIVITY = "update_activity";
    public static final String ACTION_UPDATE_NOTI = "update_notif_en";
    public static final String ACTION_UPDATE_VIB = "update_vib";
    public static final String ACTION_UPDATE_UNIT="update_unit";
    public static final String ACTION_UPDATE_SOUND="update_sound";


    // Intent Extra labels
    public static final String INTENT_DRAIN_VELOCITY = "d_vel";
    public static final String INTENT_PERCENT_FULL = "per_full";
    public static final String INTENT_NOTIF_INT = "not_int";
    public static final String INTENT_GOAL_30_SEC = "goal30sec";
    public static final String INTENT_GOAL_OZ = "goal_oz";
    public static final String INTENT_FROM_INT = "from_int";
    public static final String INTENT_TO_INT = "to_int";
    public static final String INTENT_WEIGHT = "weight";
    public static final String INTENT_ACTIVITY = "activity";
    public static final String INTENT_NOTIF_EN="notif_en";
    public static final String INTENT_VIB="notif_vib";
    public static final String INTENT_UNIT="notif_unit";
    public static final String INTENT_SOUND="notif_sound";


    /* **************** Bottle Info **************** */
    private class H2OBand_Info {
        private final int BOTTLE_MAX = 100;         // The maximum amount of water in bottle
        private final int BOTTLE_UPDATE_TIME = 100; // The amount of time to wait after updating bottle

        private int mWeight;

        private int mDrainVelocity;                 // Drain velocity (per 100 milliseconds)
        private int mPercentFull;                   // The maximum amount of water in bottle
        private int mGoal30Sec;                     // Amount of water to drink within 30 seconds

        private int mGoalOZ;                        // The goal in ounces based on a person's weight
                                                    // This value is currently only used for display

        private int mPercentFullLastCheckpoint;     // The percentage full at the last checkpoint

        // TIMING
        private int mNotificationIntervalSeconds;   // Number of seconds to wait before sending notification
        private long mLastCheckpoint;               // The time of the last checkpoint
        private long mLastUpdate;

        //Notification from time
        private int mFromSeconds;
        private int mToSeconds;

        //Activity Level
        private String activityLevel;

        private boolean mEnabled;

        private boolean mVibration;


        //is true, american, false metric
        private boolean mUnit;

        //if true-default, false ring
        private boolean mSound;

        H2OBand_Info() {
            /* Initial bottle layout_settings */
            mDrainVelocity = 0;
            mPercentFull = BOTTLE_MAX;
            mGoal30Sec = 5;

            mWeight = 120;

            mGoalOZ = 79;

            mPercentFullLastCheckpoint = BOTTLE_MAX;

            // Timing
            mNotificationIntervalSeconds = 15;
            mLastCheckpoint = System.currentTimeMillis();
            mLastUpdate = System.currentTimeMillis();

            //Notification timing
            mFromSeconds= 6;
            mToSeconds= 7;

            mEnabled=false;
            mVibration=false;
            mUnit=true;
            mSound=true;

            activityLevel = "Light";
        }

        /**
         * Increments or decrements the stored information about the bottle
         */
        void updatePercentFull() {
            if(System.currentTimeMillis() - mLastUpdate < BOTTLE_UPDATE_TIME)
                return;

            if(mPercentFull == 0)
                return;
            mPercentFull -= mDrainVelocity;
            if(mPercentFull < 0) {
                mPercentFull = 0;
            }

            mLastUpdate = System.currentTimeMillis();
        }

        /**
         * Determines whether the goal for the amount of water to drink has been satisfied
         * @return  True if the goal has been achieved
     *   *          False otherwise
         */
        boolean goalAchieved() {
            boolean achieved;

            Log.i("MainService", "Drain velocity = " + mDrainVelocity);
            Log.i("MainService", "Percent full = " + mPercentFull);
            Log.i("MainService", "Percent full at last checkpoint = " + mPercentFullLastCheckpoint);

            int goal = mNotificationIntervalSeconds * mGoal30Sec / 30;

            Log.i("MainService", "Goal = " + goal);

            achieved = mPercentFullLastCheckpoint - mPercentFull >= goal;
            mPercentFullLastCheckpoint = mPercentFull;

            return achieved;
        }

        /**
         * Determines whether the system has waited the required time interval betewen notficiations
         * @return  True if enough time has passed between last checkpoint and current time
         *          False otherwise
         */
        boolean notificationTimeIntervalAchieved() {
            // if current time is late than the from time
            Log.i("H2OBand_Info", "withinnotificationinterval = " + withinNotifInterval());
            if(withinNotifInterval())
            {
            if((System.currentTimeMillis() - mLastCheckpoint) / 1000 >=
                    mNotificationIntervalSeconds) {
                mLastCheckpoint = System.currentTimeMillis();
                return true;
            }}
            return false;
        }

        /**
         * Determines whether the current time is within the boundaries of the start time and
         * end time
         * @return  True if enough time is between the start and end times
         *          False otherwise
         */
        boolean withinNotifInterval() {

                Calendar currentTime = Calendar.getInstance();
                int hours = currentTime.get(Calendar.HOUR_OF_DAY);
            Log.i("H2OBand_Info", "Currentime is = " + hours);
            Log.i("H2OBand_Info", "from time is = " + mFromSeconds);
            Log.i("H2OBand_Info", "from time is = " + mToSeconds);

                return hours > mFromSeconds &&
                        hours < mToSeconds;

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

        void setGoalOZ(int goalOZ) {
            mGoalOZ = goalOZ;
        }

        void setLastCheckpoint(long checkpoint) {
            mLastCheckpoint = checkpoint;
        }

        void setPercentFullLastCheckpoint(int percentFullLastCheckpoint) {
            mPercentFullLastCheckpoint = percentFullLastCheckpoint;
        }

        void setActivityLevel(String activity){activityLevel = activity;}

        void setFromSeconds(int FromSeconds) {mFromSeconds = FromSeconds;}

        void setToSeconds(int ToSeconds) {mToSeconds= ToSeconds;}

        void setWeight(int weight) {
            mWeight = weight;
        }

        void setEnabled(boolean enabled){
            mEnabled= enabled;
        }

        void setVibration(boolean vibration){mVibration= vibration;}

        void setUnit(boolean unit){mUnit= unit;}

        void setSound(boolean sound){mSound= sound;}

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

        int getGoalOZ() {
            return mGoalOZ;
        }

        long getLastCheckpoint() {
            return mLastCheckpoint;
        }

        int getPercentFullLastCheckpoint() {
            return mPercentFullLastCheckpoint;
        }

        int getFromSeconds() {return mFromSeconds;}

        int getToSeconds() {return mToSeconds;}

        int getWeight() {
            return mWeight;
        }

        boolean getEnabled(){
            return mEnabled;
        }

        boolean getVibration(){return mVibration;}

        boolean getUnit(){return mUnit;}

        boolean getSound(){return mSound;}

        String getActivityLevel(){return activityLevel;}
    }
    H2OBand_Info info;

    private class TestUpdate {
        private final int INTERVAL = 2;

        private long mLastUpdate;
        private Random random;

        public TestUpdate() {
            mLastUpdate = System.currentTimeMillis();
            random = new Random();
        }

        public void update() {
            if((System.currentTimeMillis() - mLastUpdate) / 1000 >= INTERVAL) {
                int rand_d_vel = Math.abs(random.nextInt() % 3);

                Log.i("TestUpdate", "Sending new drain velocity: " + rand_d_vel);
                Intent intent = new Intent(ACTION_UPDATE_DRAIN_VELOCITY);
                intent.putExtra(INTENT_DRAIN_VELOCITY, rand_d_vel);
                sendBroadcast(intent);
                mLastUpdate = System.currentTimeMillis();
            }
        }
    }
    TestUpdate tester;




    /* **************** System **************** */
    private final H2OBandUpdateThread h2oThread = new H2OBandUpdateThread();

    NotificationCompat.Builder nBuilder;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()) {
                case ACTION_UPDATE_NOTIFICATION_INT:
                    info.setNotificationIntervalSeconds(intent.getExtras().getInt(INTENT_NOTIF_INT));
                    break;
                case ACTION_UPDATE_FROM_TIME:
                    Log.d("MainService", "BroadcastReceiver received: " +
                            intent.getExtras().getLong(INTENT_FROM_INT));
                    info.setFromSeconds(intent.getExtras().getInt(INTENT_FROM_INT));
                    break;
                case ACTION_UPDATE_TO_TIME:
                    Log.d("MainService", "BroadcastReceiver received: " +
                            intent.getExtras().getLong(INTENT_TO_INT));
                    info.setToSeconds(intent.getExtras().getInt(INTENT_TO_INT));
                    break;
                case ACTION_UPDATE_ACTIVITY:
                    Log.d("MainService", "Setting activity level");
                    Log.d("MainService", "Level: " + intent.getExtras().getString(INTENT_ACTIVITY));
                    info.setActivityLevel(intent.getExtras().getString(INTENT_ACTIVITY));
                    break;
                case ACTION_UPDATE_NOTI:
                    info.setEnabled(intent.getExtras().getBoolean(INTENT_NOTIF_EN));
                    break;
                case ACTION_UPDATE_VIB:
                    info.setVibration(intent.getExtras().getBoolean(INTENT_VIB));
                    break;
                case ACTION_UPDATE_UNIT:
                    info.setUnit(intent.getExtras().getBoolean(INTENT_UNIT));
                    break;
                case ACTION_UPDATE_SOUND:
                    info.setSound(intent.getExtras().getBoolean(INTENT_SOUND));
                    break;
                case ACTION_UPDATE_GOAL:
                    Log.d("MainService", "Updating goal");
                    info.setGoalOZ(intent.getExtras().getInt(INTENT_GOAL_OZ));
                    break;
                case ACTION_UPDATE_WEIGHT:
                    Log.d("MainService", "Updating weight");
                    info.setWeight(intent.getExtras().getInt(INTENT_WEIGHT));
                    break;
                /* case ACTION_REQUEST_INFO:
                    Intent response = new Intent(ACTION_INFO_UPDATE);
                    response.putExtra(INTENT_DRAIN_VELOCITY, info.getDrainVelocity());
                    response.putExtra(INTENT_PERCENT_FULL, info.getPercentFull());
                    response.putExtra(INTENT_NOTIF_INT, info.getNotificationIntervalSeconds());
                    response.putExtra(INTENT_GOAL_30_SEC, info.getGoal30Sec());
                    response.putExtra(INTENT_GOAL_OZ, info.getGoalOZ());
                    response.putExtra(INTENT_FROM_INT, info.getFromSeconds());
                    response.putExtra(INTENT_TO_INT, info.getToSeconds());
                    response.putExtra(INTENT_ACTIVITY, info.getActivityLevel());
                    sendBroadcast(response);
                    break; */
            }

            Intent response = new Intent(ACTION_INFO_UPDATE);
            response.putExtra(INTENT_DRAIN_VELOCITY, info.getDrainVelocity());
            response.putExtra(INTENT_PERCENT_FULL, info.getPercentFull());
            response.putExtra(INTENT_NOTIF_INT, info.getNotificationIntervalSeconds());
            response.putExtra(INTENT_GOAL_30_SEC, info.getGoal30Sec());
            response.putExtra(INTENT_GOAL_OZ, info.getGoalOZ());
            response.putExtra(INTENT_FROM_INT, info.getFromSeconds());
            response.putExtra(INTENT_TO_INT, info.getToSeconds());
            response.putExtra(INTENT_ACTIVITY, info.getActivityLevel());
            response.putExtra(INTENT_WEIGHT, info.getWeight());
            response.putExtra(INTENT_NOTIF_EN,info.getEnabled());
            response.putExtra(INTENT_VIB,info.getVibration());
            response.putExtra(INTENT_UNIT,info.getUnit());
            response.putExtra(INTENT_SOUND,info.getSound());
            sendBroadcast(response);
        }
    };









    /* **************** Implementation **************** */

    @Override
    public void onCreate() {
        super.onCreate();
        nBuilder = new NotificationCompat.Builder(this);

        info = new H2OBand_Info();
        tester = new TestUpdate();

        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_NOTIFICATION_INT);
        intentFilter.addAction(ACTION_REQUEST_INFO);
        intentFilter.addAction(ACTION_UPDATE_FROM_TIME);
        intentFilter.addAction(ACTION_UPDATE_TO_TIME);
        intentFilter.addAction(ACTION_UPDATE_GOAL);
        intentFilter.addAction(ACTION_UPDATE_ACTIVITY);
        intentFilter.addAction(ACTION_UPDATE_NOTI);
        intentFilter.addAction(ACTION_UPDATE_VIB);
        intentFilter.addAction(ACTION_UPDATE_UNIT);
        intentFilter.addAction(ACTION_UPDATE_SOUND);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        /* **************** Server layout_settings **************** */
        private final int PORT = 8080;
        private final int BACKLOG = 5;
        private final int TIMEOUT = 100;
        private ServerSocket mServerSocket;


        /**
         * Configures the server socket and initializes the initial bottle properties
         */
        H2OBandUpdateThread() {
            /* Configure server layout_settings */
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
                // Update the contents of the bottle
                info.updatePercentFull();

                String cmd = monitorSocket();
                if(cmd != null)
                    processCommand(cmd);
                checkNotification();
                //tester.update();
            }
        }





        /**
         * Checks the socket to see whether a request to change the drain velocity has arrived
         * @return  True if drain velocity has been changed
         *          False otherwise
         */
        private String monitorSocket() {
            if(mServerSocket == null)
                return null;

            final String response = "Received";

            try {
                Socket socket = mServerSocket.accept();

                /* byte counter = 0;
                int drainVelocity = 0;
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                for (byte c = (byte) input.read(); c != 0; c = (byte) input.read())
                    drainVelocity += (c << (8 * counter++));
                info.setDrainVelocity(drainVelocity);

                Log.d("MonitorThread", "mDrainVelocity = " + drainVelocity); */

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String result = "";
                for(char c = (char)input.read(); c != 0; c = (char)input.read())
                    result += c;

                OutputStream output = socket.getOutputStream();
                output.write(response.getBytes());

                socket.close();

                return result;
            } catch(IOException e) {
                return null;
            }
        }

        private void processCommand(String cmd) {
            if(cmd.contains("d_vel")) {
                Log.i("H2OUpdateThread", "Updating drain velocity");
                String[] args = cmd.split(" ");
                try {
                    int d_vel = Integer.valueOf(args[1]);
                    info.setDrainVelocity(d_vel);


                    Intent intent = new Intent(ACTION_UPDATE_DRAIN_VELOCITY);
                    intent.putExtra(INTENT_DRAIN_VELOCITY, d_vel);
                    sendBroadcast(intent);
                } catch(NumberFormatException e) {
                    e.printStackTrace();
                }
            } else if(cmd.contains("reset")) {
                Log.i("H2OUpdateThread", "Resetting");
            }
        }

        /**
         * Determines whether a notification needs to be sent out depending on the time interval
         * specified by the user and whether or not the user has fulfilled the fluid intake
         * requirement
         */
        private void checkNotification() {
            /* if(info.withinNotifInterval())
                Log.d("H2OBandUpdateThread", "Within interval");
            else
                Log.d("H2OBandUpdateThread", "Outside interval"); */

            if(info.mEnabled && info.notificationTimeIntervalAchieved()) {
                if(!info.goalAchieved()) {
                    Log.v("MainService", "Goal is not achieved");
                    sendNotification();
                }
                else {
                    Log.v("MainService", "Goal is achieved");
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
            nBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            nBuilder.setAutoCancel(true);


            Log.i("MainService", "sound = " + info.mSound);
            if(info.mSound){
                nBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);}
            else{

                nBuilder.setSound( Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.demonstrative));
            }


            if (info.mVibration){
                nBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });}


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(getApplicationContext(), 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            nBuilder.setContentIntent(pendingIntent);
            NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nManager.notify(20, nBuilder.build());
        }
    }
}
