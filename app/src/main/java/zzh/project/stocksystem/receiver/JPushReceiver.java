package zzh.project.stocksystem.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import zzh.project.stocksystem.ui.MainActivity;

/**
 * 自定义JPush广播处理
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = JPushReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("cn.jpush.android.intent.NOTIFICATION_OPENED".equals(intent.getAction())) {
            Intent toMain = new Intent(context, MainActivity.class);
            toMain.putExtra(MainActivity.ARGUMENT_PAGE_INDEX, 3);
            toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(toMain);
        }
        Log.d(TAG, "" + intent);
    }
}
