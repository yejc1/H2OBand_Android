package seniordesign.h2oband_03;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Owner on 3/10/2017.
 */

public class Tab07 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page07, container, false);

        Spinner from_dropdown = (Spinner) rootView.findViewById(R.id.from_spinner);
        String[] from_items = new String[]{"06:00","07:00","08:00","09:00", "10:00","11:00","12:00", "13:00","14:00","15:00","16:00", "17:00", "18:00", "19:00","20:00", "21:00","22:00","23:00","24:00"};
        ArrayAdapter<String> from_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, from_items);
        from_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        from_dropdown.setAdapter(from_adapter);

        Spinner to_dropdown = (Spinner) rootView.findViewById(R.id.age_spinner);
        String[] to_items = new String[]{"07:00","08:00","09:00", "10:00","11:00","12:00", "13:00","14:00","15:00","16:00", "17:00", "18:00", "19:00","20:00", "21:00","22:00","23:00","24:00"};
        ArrayAdapter<String> to_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, to_items);
        to_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        to_dropdown.setAdapter(to_adapter);

        Spinner interval_dropdown = (Spinner) rootView.findViewById(R.id.age_spinner);
        String[] interval_items = new String[]{"15", "16", "17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50"};
        ArrayAdapter<String> interval_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, interval_items);
        interval_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        interval_dropdown.setAdapter(interval_adapter);

        return rootView;
    }

}
