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
import android.widget.TextView;

public class ReportFragment extends PageFragment {

    @Override
    public void handleMessage(Message msg) {
        if(msg.what == MainActivity.INFO_UPDATE)
            if(getView() != null) {
                Log.d("ReportFragment", "Goal in ounces = " + msg.getData().getInt(MainService.INTENT_GOAL_OZ));
                TextView textView23 = (TextView) getView().findViewById(R.id.textView23);

                Log.d("ReportFragment", "Unit selection = " + msg.getData().getBoolean(MainService.INTENT_UNIT));

                //if true-american, false-metric
                if(msg.getData().getBoolean(MainService.INTENT_UNIT))
                    textView23
                        .setText(new StringBuilder(msg.getData().getInt(MainService.INTENT_GOAL_OZ) + " oz"));
                else
                    textView23
                            .setText(new StringBuilder(calculateML(msg.getData().getInt(MainService.INTENT_GOAL_OZ)) + " mL"));


                /*Log.d("ReportFragment", "Acvitiy level = " + msg.getData().getString(MainService.INTENT_ACTIVITY));
                TextView textView111 = (TextView) getView().findViewById(R.id.textView111);
                textView111
                        .setText(new StringBuilder("" + msg.getData().getString(MainService.INTENT_ACTIVITY)));

                Log.d("ReportFragment", "Weight = " + msg.getData().getInt(MainService.INTENT_WEIGHT));
                TextView textView9 = (TextView)getView().findViewById(R.id.textView9);
                textView9.setText(new StringBuilder(msg.getData().getInt(MainService.INTENT_WEIGHT) + " lb"));*/
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getContext().sendBroadcast(new Intent(MainService.ACTION_REQUEST_INFO));
        return inflater.inflate(R.layout.layout_report, container, false);
    }

    int calculateML(int oz){
        return (int)(oz * 29.5735);

    }


}

