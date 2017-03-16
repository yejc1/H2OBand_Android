package seniordesign.h2oband_03;

/**
 * Created by Owner on 1/25/2017.
 */

        import android.app.Activity;
        import android.graphics.Color;
        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.RelativeLayout;

        import com.github.mikephil.charting.charts.PieChart;

public class Tab01  extends PageFragment {

    private static String TAG ="Tab01";
    private float yData = 35;
    private String xData= "Water Consumption";
    PieChart pieChart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page01, container, false);

        RelativeLayout mealLayout = (RelativeLayout) rootView.findViewById(R.id.background);

        setView(rootView);

        return rootView;
    }

    private void setView(View view) {

    }
}
