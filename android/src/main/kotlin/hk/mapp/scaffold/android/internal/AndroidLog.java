package hk.mapp.scaffold.android.internal;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import hk.mapp.scaffold.core.base.Log;
import kotlin.jvm.functions.Function0;

public class AndroidLog implements Log {

    private final String label;

    public AndroidLog(Context context) {
        label = context.getString(context.getApplicationInfo().labelRes);
    }

    @Override public void catching(@NotNull Throwable t) {
        android.util.Log.e(label, "catching", t);
    }

    @Override public void debug(@NotNull Function0<?> msg) {
        android.util.Log.d(label, String.valueOf(msg.invoke()));
    }

    @Override public void debug(@NotNull String msg) {
        android.util.Log.d(label, msg);
    }

}
