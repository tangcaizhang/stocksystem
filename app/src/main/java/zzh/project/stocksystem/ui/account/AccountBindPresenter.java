package zzh.project.stocksystem.ui.account;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.util.Md5Util;

class AccountBindPresenter extends BasePresenter<AccountBindContract.View> implements AccountBindContract.Presenter {
    private UserModel mUserModel;

    AccountBindPresenter(AccountBindContract.View view) {
        super(view);
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void bindAccount() {
        mView.showLoading();
        mSubscription.clear();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                AccountBean accountBean = new AccountBean();
                accountBean.cardNum = mView.getCardNum();
                accountBean.idNum = mView.getIdNum();
                accountBean.realName = mView.getRealName();
                accountBean.password = Md5Util.toMD5(mView.getPassword());
                try {
                    mUserModel.bindAccount(accountBean);
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
                    mView.showMessage("绑定成功");
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

    @Override
    public void doFirst() {

    }
}
