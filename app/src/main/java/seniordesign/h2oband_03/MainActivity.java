package seniordesign.h2oband_03;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int PORT = 8000;
    private final int TIMEOUT = 500;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ServerSocket serverSocket;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        try {
            serverSocket = new ServerSocket(PORT, 5, InetAddress.getLocalHost());
            serverSocket.setSoTimeout(TIMEOUT);

        } catch(Exception e) {
            e.printStackTrace();
        }



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<PageFragment> pages;

        public SectionsPagerAdapter(FragmentManager fm) {
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

    private class MonitorThread extends Thread implements Runnable {
        private final int PORT = 8000;
        private final int BACKLOG = 5;
        private final int TIMEOUT = 100;

        @Override
        public void run() {
            ServerSocket serverSocket;
            Socket socket;

            try {
                serverSocket = new ServerSocket(PORT, BACKLOG, InetAddress.getLocalHost());
                serverSocket.setSoTimeout(TIMEOUT);
            } catch(IOException e) {
                e.printStackTrace();
            }

            while(!Thread.interrupted()) {
                try {
                    socket = serverSocket.accept();
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String result = "";
                    for(String current = input.readLine(); current != null; current = input.readLine())
                        result += current;

                    Log.d("MainActivity", result);
                } catch(Exception e) {}
            }

            try {
                serverSocket.close();
                serverSocket = null;
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}