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
    static final int INFO_UPDATE = 1;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = mViewPager.getCurrentItem();
            Message msg = new Message();

            switch(intent.getAction()) {
                case MainService.ACTION_UPDATE_DRAIN_VELOCITY:
                    Log.d("MainActivity", "d_vel = " + intent.getExtras().getInt(MainService.INTENT_DRAIN_VELOCITY));
                    msg.what = MSG_D_VEL;
                    break;
                case MainService.ACTION_INFO_UPDATE:
                    /* Log.d("MainActivity", "d_vel = " + intent.getExtras().getInt(MainService.INTENT_DRAIN_VELOCITY));
                    Log.d("MainActivity", "goal = " + intent.getExtras().getInt(MainService.INTENT_GOAL_30_SEC));
                    Log.d("MainActivity", "notif_int = " + intent.getExtras().getInt(MainService.INTENT_NOTIF_INT));
                    Log.d("MainActivity", "per_ful = " + intent.getExtras().getInt(MainService.INTENT_PERCENT_FULL));
                    Log.d("MainActivity", "goal OZ = " + intent.getExtras().getInt(MainService.INTENT_GOAL_OZ)); */
                    msg.what = INFO_UPDATE;
                    break;
            }

            msg.setData(intent.getExtras());
            ((PageFragment)mSectionsPagerAdapter.getItem(position)).handleMessage(msg);
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
        monitorThread.start(); */
        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainService.class);
        startService(intent);

        IntentFilter intentFilter = new IntentFilter(MainService.ACTION_UPDATE_DRAIN_VELOCITY);
        intentFilter.addAction(MainService.ACTION_INFO_UPDATE);
        registerReceiver(broadcastReceiver, intentFilter);






        // Clear all notifications associated with app
        /* ********************************************************************************** */
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setAutoCancel(true);
    }

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
}