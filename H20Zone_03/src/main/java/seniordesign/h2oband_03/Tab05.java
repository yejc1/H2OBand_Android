package seniordesign.h2oband_03;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.support.v4.app.Fragment;

/**
 * Created by Owner on 3/1/2017.
 */

public class Tab05 extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page05, container, false);

        RelativeLayout mealLayout = (RelativeLayout) rootView.findViewById(R.id.background);


        return rootView;
    }
}
