package zzh.project.stocksystem.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import zzh.project.stocksystem.ui.MainActivity;

/**
 * 自定义JPush广播处理
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = JPushReceiver.class.getSimpleName();
    public static final JPushObservable OBSERVABLE = new JPushObservable();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Intent toMain = new Intent(context, MainActivity.class);
            toMain.putExtra(MainActivity.ARGUMENT_PAGE_INDEX, 3);
            toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(toMain);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            final String title = intent.getStringExtra(JPushInterface.EXTRA_TITLE);
            final String message = intent.getStringExtra(JPushInterface.EXTRA_ALERT);
            // 异步一下
            rx.Observable.create(new rx.Observable.OnSubscribe<Void>() {
                @Override
                public void call(Subscriber<? super Void> subscriber) {
                    OBSERVABLE.notifyChanged(title, message);
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        }
        Log.d(TAG, "" + intent);
    }

    public static class JPushObservable extends Observable<JPushObservable.JPushObserver> {
        public interface JPushObserver {
            void onNotificationReceived(String title, String message);
        }

        /* 通知所有注册的监听者 */
        public void notifyChanged(String title, String message) {
            synchronized (mObservers) {
                for (int i = mObservers.size() - 1; i >= 0; i--) {
                    mObservers.get(i).onNotificationReceived(title, message);
                }
            }
        }
    }
}
