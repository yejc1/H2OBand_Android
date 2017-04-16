package seniordesign.h2oband_03;

import android.app.IntentService;
import android.content.Intent;
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

public class MainService extends IntentService {
    public MainService() {
        super("MainService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        final int PORT = 8080;
        final int BACKLOG = 5;
        final int TIMEOUT = 200;

        while(!Thread.interrupted()) {
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
