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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class Tab03 extends PageFragment {

    /**
     * An enumerator to specify the button that was clicked
     */
    private enum PageSelection {
        DEVICE_PAIRING,
        INITIALIZATION_SETUP,
        NOTIFICATION
    };

    @Override
    public boolean onBackPressed() {
        FrameLayout settings_frame = (FrameLayout)getView();

        // Indicates that the root view, the FrameLayout (named settings_frame), only
        // has one child, which would be the Tab03
        //
        // if the settings_frame has more than one child, it indicates that Tab03 is not
        // the view in focus, but that a button has been clicked and another window is
        // overlapping
        if(settings_frame == null || settings_frame.getChildCount() == 1)
            return false;

        settings_frame.removeViewAt(1);
        settings_frame.getChildAt(0).setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);
        setView(rootView);
        return rootView;
    }

    /**
     * Sets the current view and initializes buttons
     * @param view    The view to set
     */
    private void setView(View view) {
        LayoutInflater.from(getContext())
                .inflate(R.layout.page03, (FrameLayout)view.findViewById(R.id.settings_frame));

        view.findViewById(R.id.device_pairing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(PageSelection.DEVICE_PAIRING);
            }
        });
        view.findViewById(R.id.account_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(PageSelection.INITIALIZATION_SETUP);
            }
        });
        view.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(PageSelection.NOTIFICATION);
            }
        });
    }

    /**
     * Switches the page when a button is clicked
     * @param selection    The selected button
     */
    private void switchPage(PageSelection selection) {
        FrameLayout settings_frame = (FrameLayout)getView().findViewById(R.id.settings_frame);
        ViewGroup sub_frame = (ViewGroup)settings_frame.getChildAt(0);
        sub_frame.setVisibility(View.GONE);
        switch(selection) {
            case DEVICE_PAIRING:
                LayoutInflater.from(getContext()).inflate(R.layout.page05, settings_frame);
                setDevicePairingPage();
                break;
            case INITIALIZATION_SETUP:
                LayoutInflater.from(getContext()).inflate(R.layout.page06, settings_frame);
                setInitializationSetup();
                break;
            case NOTIFICATION:
                LayoutInflater.from(getContext()).inflate(R.layout.page04, settings_frame);
                setNotificationPage();
                break;
            default:
                Log.e(getClass().toString(), "Invalid page selection received");
        }
    }

    /**
     * Sets the device pairing page
     * Assumes that the device pairing page has already been brought to focus in the settings
     * frame
     */
    private void setDevicePairingPage() {
        FrameLayout settings_frame = (FrameLayout)getView();
        if(settings_frame != null) {
            LinearLayout notifications_frame = (LinearLayout)settings_frame.getChildAt(1);

            Spinner dropdown = (Spinner) notifications_frame.findViewById(R.id.bluetooth_spinner);
            String[] items = new String[]{"ON","OFF"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            dropdown.setAdapter(adapter);
        }
    }

    /**
     * Sets the intialization setup page
     * Assumes that the intialization setup page has already been brought to focus in the settings
     * frame
     */
    private void setInitializationSetup() {
        FrameLayout settings_frame = (FrameLayout)getView();
        if(settings_frame != null) {
            LinearLayout notifications_frame = (LinearLayout)settings_frame.getChildAt(1);

            Spinner gender_dropdown = (Spinner) notifications_frame.findViewById(R.id.gender_spinner);
            String[] gender_items = new String[]{"Female", "Male"};
            ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, gender_items);
            gender_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            gender_dropdown.setAdapter(gender_adapter);

            Spinner unit_dropdown = (Spinner) notifications_frame.findViewById(R.id.unit_spinner);
            String[] unit_items = new String[]{"Metric", "American"};
            ArrayAdapter<String> unit_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, unit_items);
            unit_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            unit_dropdown.setAdapter(unit_adapter);
        }
    }

    /**
     * Sets the notifications page
     * Assumes that the notifications page has already been brought to focus in the settings
     * frame
     */
    private void setNotificationPage() {
        FrameLayout settings_frame = (FrameLayout)getView();
        if(settings_frame != null) {
            LinearLayout notifications_frame = (LinearLayout)settings_frame.getChildAt(1);

            Spinner from_dropdown = (Spinner)notifications_frame.findViewById(R.id.from_spinner);
            String[] from_items = new String[]{"06:00","07:00","08:00","09:00", "10:00","11:00","12:00", "13:00","14:00","15:00","16:00", "17:00", "18:00", "19:00","20:00", "21:00","22:00","23:00","24:00"};
            ArrayAdapter<String> from_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, from_items);
            from_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            from_dropdown.setAdapter(from_adapter);

            Spinner to_dropdown = (Spinner)notifications_frame.findViewById(R.id.to_spinner);
            String[] to_items = new String[]{"07:00","08:00","09:00", "10:00","11:00","12:00", "13:00","14:00","15:00","16:00", "17:00", "18:00", "19:00","20:00", "21:00","22:00","23:00","24:00"};
            ArrayAdapter<String> to_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, to_items);
            to_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            to_dropdown.setAdapter(to_adapter);

            Spinner interval_dropdown = (Spinner) notifications_frame.findViewById(R.id.interval_spinner);
            String[] interval_items = new String[]{"5 min","10 min","20 min","30 min","1 hour","1.5 hour","2 hour","2.5 hour"};
            ArrayAdapter<String> interval_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, interval_items);
            interval_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            interval_dropdown.setAdapter(interval_adapter);

            Spinner sound_dropdown = (Spinner) notifications_frame.findViewById(R.id.sound_spinner);
            String[] sound_items = new String[]{"Old Phone","Bell"};
            ArrayAdapter<String> sound_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, sound_items);
            sound_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            sound_dropdown.setAdapter(sound_adapter);

            Spinner vibration_dropdown = (Spinner) notifications_frame.findViewById(R.id.vibration_spinner);
            String[] vibration_items = new String[]{"ON","OFF"};
            ArrayAdapter<String> vibration_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, vibration_items);
            vibration_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            vibration_dropdown.setAdapter(vibration_adapter);
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

