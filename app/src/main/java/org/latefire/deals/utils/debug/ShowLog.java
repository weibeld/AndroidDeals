package org.latefire.deals.utils.debug;

import android.util.Log;
import org.latefire.deals.utils.Constant;

/**
 * Created by hung.nguyen on 3/1/2016.
 */
public class ShowLog {
    public static final String TAG = "Deals";

    public static void debug(Object msg) {
        if (Constant.DEBUG) {
            Log.i(TAG, StackTraceInfo.getInvokingMethodNameFqn());
            Log.d(TAG, msg.toString());
        }
    }

    public static void error(Object msg) {
        if (Constant.DEBUG) {
            Log.i(TAG, StackTraceInfo.getInvokingFileNameFqn());
            Log.e(TAG, msg.toString());
        }
    }
}
