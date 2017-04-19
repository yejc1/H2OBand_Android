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

public class Tab02 extends PageFragment {

    @Override
    public void handleMessage(Message msg) {
        if(msg.what == MainActivity.INFO_UPDATE)
            if(getView() != null) {
                Log.d("Tab02", "Goal in ounces = " + msg.getData().getInt(MainService.INTENT_GOAL_OZ));
                TextView textView23 = (TextView) getView().findViewById(R.id.textView23);
                textView23
                        .setText(new StringBuilder("" + msg.getData().getInt(MainService.INTENT_GOAL_OZ)));
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getContext().sendBroadcast(new Intent(MainService.ACTION_REQUEST_INFO));
        return inflater.inflate(R.layout.page02, container, false);
    }


}

