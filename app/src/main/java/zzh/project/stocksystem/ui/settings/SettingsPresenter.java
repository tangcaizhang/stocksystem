package zzh.project.stocksystem.ui.settings;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.helper.JPushHelper;
import zzh.project.stocksystem.model.SettingsModel;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.SettingsModelImpl;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class SettingsPresenter extends BasePresenter<SettingsContract.View> implements SettingsContract.Presenter {
    private UserModel mUserModel;
    private SettingsModel mSettingModel;

    SettingsPresenter(SettingsContract.View view) {
        super(view);
        mUserModel = UserModelImpl.getInstance();
        mSettingModel = SettingsModelImpl.getInstance();
    }

    @Override
    public void doFirst() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(mSettingModel.isEnablePush());
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
                mSettingModel.enablePush();
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
                mSettingModel.disablePush();
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
                mUserModel.logout();
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
