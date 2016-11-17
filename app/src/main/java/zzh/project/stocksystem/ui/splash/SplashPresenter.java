package zzh.project.stocksystem.ui.splash;


import android.os.Handler;
import android.os.SystemClock;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.helper.JPushHelper;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {
    private UserModelImpl mUserModel;
    private Handler mHandler = new Handler();
    private Runnable mGoTask;

    SplashPresenter(SplashContract.View view) {
        super(view);
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void doFirst() {
        doInit();
    }

    @Override
    public void destroy() {
        if (mGoTask != null) {
            mHandler.removeCallbacks(mGoTask);
            mGoTask = null;
        }
        super.destroy();
    }

    @Override
    public void doInit() {
        final long start = SystemClock.currentThreadTimeMillis();
        mSubscription.clear();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(mUserModel.checkAccessToken());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mGoTask = new Runnable() {
                    @Override
                    public void run() {
                        mView.toLoginActivity();
                    }
                };
                mHandler.postDelayed(mGoTask, 1000 - (SystemClock.currentThreadTimeMillis() - start));
            }

            @Override
            public void onNext(final Boolean bSucc) {
                mGoTask = new Runnable() {
                    @Override
                    public void run() {
                        if (bSucc) {
                            mView.toMainActivity();
                            JPushHelper.setAlias(mUserModel.getHistoryUser());
                        } else {
                            mView.toLoginActivity();
                        }
                    }
                };
                mHandler.postDelayed(mGoTask, 1000 - (SystemClock.currentThreadTimeMillis() - start));
            }
        });
        mSubscription.add(subscription);
    }
}
