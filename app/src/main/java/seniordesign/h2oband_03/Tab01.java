package seniordesign.h2oband_03;

/**
 * Created by Owner on 1/25/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;

public class Tab01 extends PageFragment {

    private static String TAG ="Tab01";
    private float yData = 35;
    private String xData= "Water Consumption";
    PieChart pieChart;


    @Override
    public void handleMessage(Message msg) {
        if(getView() != null) {
            BottleSurfaceView bottleSurfaceView =
                    (BottleSurfaceView) ((ViewGroup) getView()).getChildAt(0);
            bottleSurfaceView.changeBottleDVel(msg.getData().getInt(MainService.INTENT_DRAIN_VELOCITY));
            if(msg.what == MainActivity.INFO_UPDATE)
                bottleSurfaceView.setBottlePercentage(msg.getData().getInt(MainService.INTENT_PERCENT_FULL));
        }

        /* switch(msg.what) {
            case MainActivity.MSG_D_VEL:
                if(getView() != null) {
                    BottleSurfaceView bottleSurfaceView =
                            (BottleSurfaceView) ((ViewGroup) getView()).getChildAt(0);
                    bottleSurfaceView.changeBottleDVel(msg.getData().getInt(MainService.INTENT_DRAIN_VELOCITY));
                }
                break;
            case MainActivity.INFO_UPDATE:
                break;
        } */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //RelativeLayout mealLayout = (RelativeLayout) rootView.findViewById(R.id.background);

        getContext().sendBroadcast(new Intent(MainService.ACTION_REQUEST_INFO));
        return inflater.inflate(R.layout.page01, container, false);
    }
}
