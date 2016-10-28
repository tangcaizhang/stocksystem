package zzh.project.stocksystem.util;


import android.widget.Toast;

import java.lang.ref.WeakReference;

import zzh.project.stocksystem.MyApplication;

public class ToastUtil {
    private static WeakReference<Toast> mToastRef = new WeakReference<Toast>(null);

    private ToastUtil() {
    }

    public static void show(CharSequence text, int duration) {
        Toast ref = mToastRef.get();
        if (ref != null) {
            ref.cancel();
        }
        Toast toast = Toast.makeText(MyApplication.getInstance(), text, duration);
        mToastRef = new WeakReference<Toast>(toast);
        toast.show();
    }
}
