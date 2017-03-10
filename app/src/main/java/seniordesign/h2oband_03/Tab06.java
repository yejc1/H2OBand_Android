package seniordesign.h2oband_03;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * Created by Owner on 3/9/2017.
 */

public class Tab06 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page06, container, false);

        Spinner dropdown = (Spinner) rootView.findViewById(R.id.age_spinner);
        String[] items = new String[]{"15", "16", "17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dropdown.setAdapter(adapter);

        Spinner weight_dropdown = (Spinner) rootView.findViewById(R.id.weight_spinner);
        String[] age_items = new String[]{"15", "16", "17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50"};
        ArrayAdapter<String> age_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, age_items);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        weight_dropdown.setAdapter(age_adapter);

        Spinner gender_dropdown = (Spinner) rootView.findViewById(R.id.gender_spinner);
        String[] gender_items = new String[]{"Female", "Male"};
        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, gender_items);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gender_dropdown.setAdapter(gender_adapter);

        Spinner unit_dropdown = (Spinner) rootView.findViewById(R.id.gender_spinner);
        String[] unit_items = new String[]{"Metric", "American"};
        ArrayAdapter<String> unit_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, unit_items);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        unit_dropdown.setAdapter(unit_adapter);

        return rootView;
    }

}
