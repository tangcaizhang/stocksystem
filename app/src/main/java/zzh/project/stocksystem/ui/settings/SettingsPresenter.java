package zzh.project.stocksystem.ui.settings;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.helper.JPushHelper;
import zzh.project.stocksystem.model.SettingsManager;
import zzh.project.stocksystem.model.UserManager;
import zzh.project.stocksystem.model.impl.SettingsManagerImpl;
import zzh.project.stocksystem.model.impl.UserManagerImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class SettingsPresenter extends BasePresenter<SettingsContract.View> implements SettingsContract.Presenter {
    private UserManager mUserManager;
    private SettingsManager mSettingManager;

    SettingsPresenter(SettingsContract.View view) {
        super(view);
        mUserManager = UserManagerImpl.getInstance();
        mSettingManager = SettingsManagerImpl.getInstance();
    }

    @Override
    public void doFirst() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(mSettingManager.isEnablePush());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isEnable) {
                if (mView != null && mView.isActive()) {
                    mView.switchPush(isEnable);
                }
            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void enablePush() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                mSettingManager.enablePush();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.switchPush(true);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {

            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void disablePush() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                mSettingManager.disablePush();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.switchPush(false);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {

            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void logout() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                mUserManager.logout();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.toLoginActivity();
                }
                // 修改别名，防止收到推送
                JPushHelper.setAlias("null");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {

            }
        });
        mSubscription.add(subscription);
    }
}
