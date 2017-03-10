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

        Spinner from_dropdown = (Spinner) rootView.findViewById(R.id.from_init_spinner);
        String[] from_items = new String[]{"06:00","07:00","08:00","09:00", "10:00","11:00","12:00", "13:00","14:00","15:00","16:00", "17:00", "18:00", "19:00","20:00", "21:00","22:00","23:00","24:00"};
        ArrayAdapter<String> from_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, from_items);
        from_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        from_dropdown.setAdapter(from_adapter);

        Spinner to_dropdown = (Spinner) rootView.findViewById(R.id.to_init_spinner);
        String[] to_items = new String[]{"07:00","08:00","09:00", "10:00","11:00","12:00", "13:00","14:00","15:00","16:00", "17:00", "18:00", "19:00","20:00", "21:00","22:00","23:00","24:00"};
        ArrayAdapter<String> to_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, to_items);
        to_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        to_dropdown.setAdapter(to_adapter);

        Spinner interval_dropdown = (Spinner) rootView.findViewById(R.id.interval_init_spinner);
        String[] interval_items = new String[]{"5 min","10 min","20 min","30 min","1 hour","1.5 hour","2 hour","2.5 hour"};
        ArrayAdapter<String> interval_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, interval_items);
        interval_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        interval_dropdown.setAdapter(interval_adapter);

        Spinner sound_dropdown = (Spinner) rootView.findViewById(R.id.sound_init_spinner);
        String[] sound_items = new String[]{"Old Phone","Bell"};
        ArrayAdapter<String> sound_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, sound_items);
        sound_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sound_dropdown.setAdapter(sound_adapter);

        Spinner vibration_dropdown = (Spinner) rootView.findViewById(R.id.vibration_init_spinner);
        String[] vibration_items = new String[]{"ON","OFF"};
        ArrayAdapter<String> vibration_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, vibration_items);
        vibration_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        vibration_dropdown.setAdapter(vibration_adapter);


        return rootView;
    }

}
