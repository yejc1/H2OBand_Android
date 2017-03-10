package seniordesign.h2oband_03;


import android.support.v4.app.Fragment;
import android.view.View;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Owner on 3/8/2017.
 */

public class NotificationCenter extends ActionBarActivity{

    NotificationCompat.Builder notification;
    private static final int uniqueID= 45512;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
    }





public void setAlarm(View veiw){
    notification.setSmallIcon(R.drawable.water_bottle);
    notification.setTicker("This is the ticket");
    notification.setWhen(System.currentTimeMillis());
    notification.setContentTitle("H20 Band");
    notification.setContentText("Time to drink water");

    Intent intent= new Intent(this, NotificationCenter.class);
    PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    notification.setContentIntent(pendingIntent);

    NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    nm.notify(uniqueID,notification.build());


}

}
