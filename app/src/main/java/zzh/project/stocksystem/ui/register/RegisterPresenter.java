package zzh.project.stocksystem.ui.register;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.impl.UserManagerImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.util.Md5Util;

class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
    private UserManagerImpl mUserModel;

    RegisterPresenter(RegisterContract.View view) {
        super(view);
        mUserModel = UserManagerImpl.getInstance();
    }

    @Override
    public void doFirst() {

    }

    @Override
    public void register() {
        mView.showLoading();
        mSubscription.clear();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<UserBean>() {
            @Override
            public void call(Subscriber<? super UserBean> subscriber) {
                UserBean bean = new UserBean();
                bean.username = mView.getUsername().trim();
                bean.nick = mView.getNick().trim();
                bean.email = mView.getEmail().trim();
                bean.password = Md5Util.toMD5(mView.getPassword().trim());
                try {
                    mUserModel.register(bean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).flatMap(new Func1<UserBean, Observable<Void>>() {
            @Override
            public Observable<Void> call(final UserBean userBean) {
                return Observable.create(new Observable.OnSubscribe<Void>() {
                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        try {
                            mUserModel.register(userBean);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showMessage("注册成功");
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
