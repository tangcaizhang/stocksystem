package zzh.project.stocksystem.model.impl;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.bean.NewsBean;
import zzh.project.stocksystem.db.dao.NewsDao;
import zzh.project.stocksystem.model.NewsManager;
import zzh.project.stocksystem.receiver.JPushReceiver;

public class NewsManagerImpl implements NewsManager {
    private final String TAG = this.getClass().getSimpleName();
    private Map<OnNewsChangedListener, JPushReceiver.JPushObservable.JPushObserver> mListenerMapper = new HashMap<>();
    private static NewsManagerImpl sInstance;
    private NewsDao mNewsDao;

    private NewsManagerImpl(Context context) {
        mNewsDao = NewsDao.getInstance();
    }

    public static NewsManagerImpl getInstance() {
        if (sInstance == null) {
            sInstance = new NewsManagerImpl(MyApplication.getInstance());
        }
        return sInstance;
    }

    @Override
    public Observable<List<NewsBean>> listNews() {
        return Observable.create(new Observable.OnSubscribe<List<NewsBean>>() {
            @Override
            public void call(Subscriber<? super List<NewsBean>> subscriber) {
                subscriber.onNext(mNewsDao.findAll());
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void register(final OnNewsChangedListener listener) {
        if (mListenerMapper.containsKey(listener)) {
            return;
        }
        JPushReceiver.JPushObservable.JPushObserver observer = new JPushReceiver.JPushObservable.JPushObserver() {
            @Override
            public void onNotificationReceived(String title, String message) {
                listener.onNewsChanged();
            }
        };
        mListenerMapper.put(listener, observer);
    }

    @Override
    public void unregister(OnNewsChangedListener listener) {
        if (!mListenerMapper.containsKey(listener)) {
            return;
        }
        JPushReceiver.JPushObservable.JPushObserver observer = mListenerMapper.get(listener);
        JPushReceiver.OBSERVABLE.unregisterObserver(observer);
        mListenerMapper.remove(listener);
    }

    public void destroy() {
        mNewsDao = null;
    }
}
