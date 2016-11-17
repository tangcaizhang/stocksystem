package zzh.project.stocksystem.ui.recharge;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.UserManager;
import zzh.project.stocksystem.model.impl.UserManagerImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.util.Md5Util;

class RechargePresenter extends BasePresenter<RechargeContract.View> implements RechargeContract.Presenter {
    private UserManager mUserManager;

    RechargePresenter(RechargeContract.View view) {
        super(view);
        mUserManager = UserManagerImpl.getInstance();
    }

    @Override
    public void doFirst() {

    }

    @Override
    public void recharge() {
        mView.showLoading();
        mSubscription.clear();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                try {
                    String cardNum = mView.getCardNum();
                    String pass = Md5Util.toMD5(mView.getPassword());
                    float money = mView.getMoney();
                    mUserManager.recharge(cardNum, pass, money);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showMessage("充值成功");
                    mView.close();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showErrorMessage(MsgHelper.getErrorMsg(e));
                }
            }

            @Override
            public void onNext(Void aVoid) {

            }
        });
        mSubscription.add(subscription);
    }
}
