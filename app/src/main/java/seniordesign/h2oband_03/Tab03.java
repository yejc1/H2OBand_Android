package seniordesign.h2oband_03;

/**
 * Created by Owner on 1/25/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Tab03 extends PageFragment {

    private enum PageSelection {
        DEVICE_PAIRING,
        NOTIFICATION
    };

    @Override
    public boolean onBackPressed() {
        return true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);
        setView(rootView);
        return rootView;
    }

    private void setView(View view) {
        LayoutInflater.from(getContext())
                .inflate(R.layout.page03, (FrameLayout)view.findViewById(R.id.settings_frame));


        TextView device_pairing = (TextView)view.findViewById(R.id.device_pairing),
                notification = (TextView)view.findViewById(R.id.notification);
        device_pairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(PageSelection.DEVICE_PAIRING);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(PageSelection.NOTIFICATION);
            }
        });
    }

    private void switchPage(PageSelection selection) {
        FrameLayout settings_frame = (FrameLayout)getView().findViewById(R.id.settings_frame);
        ViewGroup sub_frame = (ViewGroup)settings_frame.getChildAt(0);
        sub_frame.setVisibility(View.GONE);
        switch(selection) {
            case DEVICE_PAIRING:
                LayoutInflater.from(getContext()).inflate(R.layout.page05, settings_frame);
                break;
            case NOTIFICATION:
                LayoutInflater.from(getContext()).inflate(R.layout.page04, settings_frame);
                break;
            default:
                Log.e(getClass().toString(), "Invalid page selection received");
        }
    }

    //@Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.device_pairing:
                fragment = new Tab05();
                replaceFragment(fragment);
                break;

            case R.id.goal:
                fragment = new Tab02();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.page03, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

