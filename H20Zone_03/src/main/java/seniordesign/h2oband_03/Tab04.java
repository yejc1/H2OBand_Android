package seniordesign.h2oband_03;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Owner on 3/1/2017.
 */

public class Tab04 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page04, container, false);

        RelativeLayout mealLayout = (RelativeLayout) rootView.findViewById(R.id.background);

        NotificationCenter noticenter= new NotificationCenter();

        noticenter.setAlarm();


        return rootView;
    }
}
