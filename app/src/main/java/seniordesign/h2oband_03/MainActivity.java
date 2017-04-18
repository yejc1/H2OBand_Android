package seniordesign.h2oband_03;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int MSG_D_VEL = 0;



    MonitorThread monitorThread = null;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MainService.ACTION_UPDATE_DRAIN_VELOCITY)) {
                Log.d("MainActivity", "d_vel = " + intent.getExtras().getInt(MainService.INTENT_DRAIN_VELOCITY));
                int position = mViewPager.getCurrentItem();
                Message msg = new Message();
                msg.what = MSG_D_VEL;
                msg.setData(intent.getExtras());
                ((PageFragment)mSectionsPagerAdapter.getItem(position)).handleMessage(msg);
            } else if(intent.getAction().equals(MainService.ACTION_INFO_UPDATE)) {
                Log.d("MainActivity", "d_vel = " + intent.getExtras().getInt(MainService.INTENT_DRAIN_VELOCITY));
                Log.d("MainActivity", "goal = " + intent.getExtras().getInt(MainService.INTENT_GOAL_30_SEC));
                Log.d("MainActivity", "notif_int = " + intent.getExtras().getInt(MainService.INTENT_NOTIF_INT));
                Log.d("MainActivity", "per_ful = " + intent.getExtras().getInt(MainService.INTENT_PERCENT_FULL));

            }
        }
    };

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // App settings
        /* ********************************************************************************** */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);






        // Service settings
        /* ********************************************************************************** */

        /* monitorThread = new MonitorThread();
        monitorThread.start();
        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_SHORT).show(); */

        Intent intent = new Intent(getApplicationContext(), MainService.class);
        startService(intent);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MainService.ACTION_UPDATE_DRAIN_VELOCITY);
        intentFilter.addAction(MainService.ACTION_INFO_UPDATE);
        registerReceiver(broadcastReceiver, intentFilter);






        // Clear all notifications associated with app
        /* ********************************************************************************** */
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setAutoCancel(true);
    }

    /* @Override
    public void onResume() {
        super.onResume();
        if(monitorThread != null && !monitorThread.isAlive())
            monitorThread.start();
    } */

    /* @Override
    public void onStop() {
        super.onStop();
        if(monitorThread != null && monitorThread.isAlive() && !monitorThread.isInterrupted())
            monitorThread.interrupt();
    } */

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        int position = mViewPager.getCurrentItem();
        if(!((PageFragment)mSectionsPagerAdapter.getItem(position))
                .onBackPressed())
            super.onBackPressed();
    }





    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<PageFragment> pages;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            pages = new ArrayList<>();
            fillPageList();
        }

        private void fillPageList() {
            pages.add(new Tab01());
            pages.add(new Tab02());
            pages.add(new Tab03());
        }

        @Override
        public Fragment getItem(int position) {
            if(position > pages.size())
                return null;
            return pages.get(position);

            /* switch (position) {
                case 0:
                    return new Tab01();
                case 1:
                    return new Tab02();
                case 2:
                    return new Tab03();
                case 3:
                    return new Tab04();
                case 4:
                    return new Tab05();
                case 5:
                    return new Tab06();
                case 6:
                    return new Tab07();

                default:
                    return null;
            } */
        }

        @Override
        public int getCount() {
            // Show 3 total pages instead of 7
            return pages.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Report";
                case 2:
                    return "Setting";
                case 3:
                    return "Noti";
                case 4:
                    return "Bluetooth";
                case 5:
                    return "Init";
                case 6:
                    return "Init2";
                default:
                    return "";
            }
        }
    }

    private class SendThread extends Thread implements Runnable {
        @Override
        public void run() {
            try {
                String text = "halt";

                Socket socket = new Socket("192.168.1.101", 80);
                OutputStream output = socket.getOutputStream();
                output.write(text.getBytes());

                String response = "";
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                for(String current = input.readLine(); current != null; current = input.readLine())
                    response += current;
                Log.d("SendThread", response);
            } catch(IOException e) {
                e.printStackTrace();
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