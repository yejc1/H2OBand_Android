package seniordesign.h2oband_03;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by Owner on 3/9/2017.
 */

public class SettingsInfoFragment extends PageFragment {


    int age = 21;
    int weight = 110;
    int activity_selection=0;
    int gender_selection = 0;
    int unit_selection = 0;

    String activity="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_info, container, false);
        setAccountInfoPage(rootView);
        return rootView;
    }

    /**
     * Sets the intialization setup page
     * Assumes that the intialization setup page has already been brought to focus in the layout_settings
     * frame
     */
    private void setAccountInfoPage(View view) {

        TextView age_edit = (TextView)view.findViewById(R.id.age_edit);
        age_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    age = Integer.parseInt(charSequence.toString());
                } catch(NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Log.d("SettingInfoFragment", "Setting age: " + age);
        age_edit.setText(new StringBuilder("" + age));


        TextView weight_edit = (TextView)view.findViewById(R.id.weight_edit);
        weight_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    weight = Integer.parseInt(charSequence.toString());
                } catch(NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateGoal();
            }
        });
        Log.d("SettingInfoFragment", "Setting weight: " + weight);
        weight_edit.setText(new StringBuilder("" + weight));

        Spinner gender_dropdown = (Spinner) view.findViewById(R.id.gender_spinner);
        String[] gender_items = new String[]{"Female", "Male"};
        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, gender_items);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gender_dropdown.setAdapter(gender_adapter);
        gender_dropdown.setSelection(gender_selection);
        gender_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender_selection = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner activity_dropdown = (Spinner) view.findViewById(R.id.activity_spinner);
        String[] activity_items = new String[]{"Light", "Medium", "Heavy"};
        final ArrayAdapter<String> activity_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, activity_items);
        activity_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        activity_dropdown.setAdapter(activity_adapter);
        activity_dropdown.setSelection(activity_selection);
        activity_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity_selection = i;
                activity = activity_adapter.getItem(i);
                    /* String sel_item = activity_adapter.getItem(i);

                    if(sel_item == null)
                        return;

                    switch(sel_item) {
                        case "Light":
                            activity = "Light";
                            break;
                        case "Medium":
                            activity = "Medium";
                            break;
                        default:
                            activity = "Heavy";
                            break;
                    } */

                Intent intent = new Intent(MainService.ACTION_UPDATE_ACTIVITY);
                intent.putExtra(MainService.INTENT_ACTIVITY, activity);
                getContext().sendBroadcast(intent);

                updateGoal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner unit_dropdown = (Spinner) view.findViewById(R.id.unit_spinner);
        String[] unit_items = new String[]{"Metric", "American"};
        ArrayAdapter<String> unit_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, unit_items);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        unit_dropdown.setAdapter(unit_adapter);
        unit_dropdown.setSelection(unit_selection);
        unit_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit_selection = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateGoal() {
        int new_goal = weight * 2 / 3;
        switch(activity) {
            case "Light":
                new_goal += 6;
                break;
            case "Medium":
                new_goal += 8;
                break;
            default:
                new_goal += 10;
                break;

        }
        Log.d("SettingFragment", "new_goal = " + new_goal);

        Intent intent = new Intent(MainService.ACTION_UPDATE_GOAL);
        intent.putExtra(MainService.INTENT_GOAL_OZ, new_goal);
        getContext().sendBroadcast(intent);
    }
}
