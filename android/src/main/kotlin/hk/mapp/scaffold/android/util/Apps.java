package hk.mapp.scaffold.android.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Looper;

import java.util.Locale;

public class Apps {

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static Locale currentLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    public static void register(Context context, String action, BroadcastReceiver receiver) {
        context.registerReceiver(receiver, new IntentFilter(action));
    }

}
