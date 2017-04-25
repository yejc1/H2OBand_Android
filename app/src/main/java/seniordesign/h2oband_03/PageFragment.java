package seniordesign.h2oband_03;

import android.os.Message;
import android.support.v4.app.Fragment;

/**
 * Created by saumil on 3/15/17.
 */

public class PageFragment extends Fragment {

    /**
     * Handles the message sent from the MainActivity from the BroadcastReceiver
     * @param msg    The message to handle
     */
    public void handleMessage(Message msg) {}

    /**
     * Handles the back pressed button
     * @return  true if the event was handled
     *          false otherwise
     */
    public boolean onBackPressed() {
        return false;
    }
}
