package zzh.project.stocksystem.model.impl;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
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
                /*
                 *  这里合理是转多一层，model内部的实例引用不应该抛出去，
                 *  如果model层对实例有操作的话，UI层直接引用起来会有并发、同步锁阻塞问题
                */
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
                Observable.just(new NewsBean(title, message)).observeOn(AndroidSchedulers.mainThread()).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        listener.onNewsChanged();
                    }
                }).observeOn(Schedulers.io()).subscribe(new Action1<NewsBean>() {
                    @Override
                    public void call(NewsBean newsBean) {
                        mNewsDao.save(newsBean);
                    }
                });
            }
        };
        JPushReceiver.OBSERVABLE.registerObserver(observer);
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
