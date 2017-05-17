package seniordesign.h2oband_03;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by Owner on 3/1/2017.
 */

public class SettingsNotifFragment extends PageFragment {

    int notification_int_selection = 0;

    int from_int_selection= 0;
    int to_int_selection= 0;
    int enabled_int_selection=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_notif, container, false);
        setNotificationPage(rootView);
        return rootView;
    }



    /**
     * Sets the notifications page
     * Assumes that the notifications page has already been brought to focus in the layout_settings
     * frame
     */
    private void setNotificationPage(View view) {
        LinearLayout notifications_frame = (LinearLayout)view;

        Spinner enabled_dropdown = (Spinner)notifications_frame.findViewById(R.id.enabled_dropdown);
        String[] enabled_items = new String[]{ "NO","YES"};
        final ArrayAdapter<String> enabled_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, enabled_items);
        enabled_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        enabled_dropdown.setAdapter(enabled_adapter);
        enabled_dropdown.setSelection(enabled_int_selection);
        enabled_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enabled_int_selection = i;
                String sel_item=enabled_adapter.getItem(i);
                //int enabled_button;
                boolean enabled_button;

                if(sel_item == null)
                    return;

                switch(sel_item) {
                    case "YES":
                        enabled_button = true;
                        break;
                    default:
                        enabled_button = false;
                        break;
                }

                Intent intent = new Intent(MainService.ACTION_UPDATE_NOTI);
                intent.putExtra(MainService.INTENT_NOTIF_EN, enabled_button);
                getContext().sendBroadcast(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner from_dropdown = (Spinner)notifications_frame.findViewById(R.id.from_spinner);
        String[] from_items = new String[]{"06:00","07:00","08:00","09:00", "10:00","11:00","12:00", "13:00","14:00","15:00","16:00", "17:00", "18:00", "19:00","20:00", "21:00","22:00","23:00","24:00"};
        final ArrayAdapter<String> from_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, from_items);
        from_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        from_dropdown.setAdapter(from_adapter);
        from_dropdown.setSelection(from_int_selection);
        from_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from_int_selection = i;
                String sel_item=from_adapter.getItem(i);
                int num_from_interval;

                if(sel_item == null)
                    return;


                switch(sel_item) {
                    case "06:00":
                        num_from_interval = 6;
                        break;
                    case "07:00":
                        num_from_interval = 7;
                        break;
                    case "08:00":
                        num_from_interval = 8;
                        break;
                    case "09:00":
                        num_from_interval = 9;
                        break;
                    case "10:00":
                        num_from_interval = 10;
                        break;
                    case "11:00":
                        num_from_interval = 11;
                        break;
                    case "12:00":
                        num_from_interval = 12;
                        break;
                    case "13:00":
                        num_from_interval = 13;
                        break;
                    case "14:00":
                        num_from_interval = 14;
                        break;
                    case "15:00":
                        num_from_interval = 15;
                        break;
                    case "16:00":
                        num_from_interval = 16;
                        break;
                    case "17:00":
                        num_from_interval = 17;
                        break;
                    case "18:00":
                        num_from_interval = 18;
                        break;
                    case "19:00":
                        num_from_interval = 19;
                        break;
                    case "20:00":
                        num_from_interval = 20;
                        break;
                    case "21:00":
                        num_from_interval = 21;
                        break;
                    case "22:00":
                        num_from_interval = 22;
                        break;
                    default:
                        num_from_interval = 23;
                        break;
                }

                Intent intent = new Intent(MainService.ACTION_UPDATE_FROM_TIME);
                intent.putExtra(MainService.INTENT_FROM_INT, num_from_interval);
                getContext().sendBroadcast(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner to_dropdown = (Spinner)notifications_frame.findViewById(R.id.to_spinner);
        String[] to_items = new String[]{"07:00","08:00","09:00", "10:00","11:00","12:00", "13:00","14:00","15:00","16:00", "17:00", "18:00", "19:00","20:00", "21:00","22:00","23:00","24:00"};
        final ArrayAdapter<String> to_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, to_items);
        to_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        to_dropdown.setAdapter(to_adapter);
        to_dropdown.setSelection(to_int_selection);
        to_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to_int_selection = i;
                String sel_item= to_adapter.getItem(i);
                int num_from_interval;

                if(sel_item == null)
                    return;


                switch(sel_item) {

                    case "07:00":
                        num_from_interval = 7;
                        break;
                    case "08:00":
                        num_from_interval = 8;
                        break;
                    case "09:00":
                        num_from_interval = 9;
                        break;
                    case "10:00":
                        num_from_interval = 10;
                        break;
                    case "11:00":
                        num_from_interval = 11;
                        break;
                    case "12:00":
                        num_from_interval = 12;
                        break;
                    case "13:00":
                        num_from_interval = 13;
                        break;
                    case "14:00":
                        num_from_interval = 14;
                        break;
                    case "15:00":
                        num_from_interval = 15;
                        break;
                    case "16:00":
                        num_from_interval = 16;
                        break;
                    case "17:00":
                        num_from_interval = 17;
                        break;
                    case "18:00":
                        num_from_interval = 18;
                        break;
                    case "19:00":
                        num_from_interval = 19;
                        break;
                    case "20:00":
                        num_from_interval = 20;
                        break;
                    case "21:00":
                        num_from_interval = 21;
                        break;
                    case "22:00":
                        num_from_interval = 22;
                        break;
                    case "23:00":
                        num_from_interval = 23;
                        break;
                    default:
                        num_from_interval = 24;
                        break;
                }

                Intent intent = new Intent(MainService.ACTION_UPDATE_TO_TIME);
                intent.putExtra(MainService.INTENT_TO_INT, num_from_interval);
                getContext().sendBroadcast(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner interval_dropdown = (Spinner) notifications_frame.findViewById(R.id.interval_spinner);
        //String[] interval_items = new String[]{"5 min","10 min","20 min","30 min","1 hour","1.5 hour","2 hour","2.5 hour"};
        String[] interval_items = new String[]{"15 sec","25 sec","35 sec","45 sec", "1 min"};
        final ArrayAdapter<String> interval_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, interval_items);
        interval_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        interval_dropdown.setAdapter(interval_adapter);
        interval_dropdown.setSelection(notification_int_selection);
        interval_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                notification_int_selection = i;
                String sel_item = interval_adapter.getItem(i);
                int num_seconds_interval;

                if(sel_item == null)
                    return;


                switch(sel_item) {
                    case "15 sec":
                        num_seconds_interval = 15;
                        break;
                    case "25 sec":
                        num_seconds_interval = 25;
                        break;
                    case "35 sec":
                        num_seconds_interval = 35;
                        break;
                    case "45 sec":
                        num_seconds_interval = 45;
                        break;
                    default:
                        num_seconds_interval = 60;
                        break;
                }

                Intent intent = new Intent(MainService.ACTION_UPDATE_NOTIFICATION_INT);
                intent.putExtra(MainService.INTENT_NOTIF_INT, num_seconds_interval);
                getContext().sendBroadcast(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
