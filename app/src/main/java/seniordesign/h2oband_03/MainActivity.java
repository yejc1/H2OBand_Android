package seniordesign.h2oband_03;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "Starting app");

        /*
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
       */

        /*
        Log.d(TAG,"onCreate: starting to create chart");
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        addDataSet(pieChart);
*/
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




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
}
