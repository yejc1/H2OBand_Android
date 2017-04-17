package seniordesign.h2oband_03;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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

    //private final MonitorThread monitorThread = new MonitorThread();
    private final RunThread runThread = new RunThread();

    NotificationCompat.Builder nBuilder;





    @Override
    public void onCreate() {
        super.onCreate();
        nBuilder = new NotificationCompat.Builder(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //monitorThread.start();
        if(!runThread.isAlive() || runThread.isInterrupted())
            runThread.start();

        return START_STICKY;
    }






    private void sendNotification() {
        nBuilder.setSmallIcon(R.drawable.bottle);
        nBuilder.setTicker("You need to drink more water");
        nBuilder.setWhen(System.currentTimeMillis());
        nBuilder.setContentTitle("H2OBand");
        nBuilder.setContentText("you need to drink more water");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(pendingIntent);
        NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nManager.notify(20, nBuilder.build());
    }





    private class RunThread extends Thread implements Runnable {
        @Override
        public void run() {
            Log.d("MainService", "Starting thread");

            int x = 0;

            while(!Thread.interrupted()) {
                Log.d("RunThread", "x = " + x);
                Intent intent = new Intent("update");
                intent.putExtra("val", x);
                sendBroadcast(intent);
                x++;

                sendNotification();

                try {
                    Thread.sleep(2000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
