package app.appified.Utils;

import android.util.Log;


public class LogUtil {
    public static String TAG = "Appified";

    public static void debug(String msg) {
        if (msg != null)
            Log.d(TAG, msg);
    }
    public static void debug(Boolean msg) {
        if (msg != null)
            Log.d(TAG, String.valueOf(msg));
    }

    public static void debug(int msg) {
        Log.d(TAG, String.valueOf(msg));
    }

    public static void info(String msg) {
        if (msg != null)
            Log.i(TAG, msg);
    }

    public static void info(int msg) {
        Log.i(TAG, String.valueOf(msg));
    }
}
