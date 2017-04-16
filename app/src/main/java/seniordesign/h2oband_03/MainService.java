package seniordesign.h2oband_03;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //new MonitorThread().start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.interrupted()) {
                    Log.d("MainService", "Running main service");

                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return START_STICKY;
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
