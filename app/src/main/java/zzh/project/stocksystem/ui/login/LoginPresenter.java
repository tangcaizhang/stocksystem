package zzh.project.stocksystem.ui.login;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import zzh.project.stocksystem.bean.AccessToken;
import zzh.project.stocksystem.helper.JPushHelper;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.util.Md5Util;

class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private UserModelImpl mUserModel;

    LoginPresenter(LoginContract.View view) {
        super(view);
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void login() {
        final String username = mView.getUsername();
        final String password = Md5Util.toMD5(mView.getPassword());
        mUserModel.setHistoryUser(username);

        mView.showLoading();
        mSubscription.clear();
        Subscription subscription = mUserModel.login(username, password).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AccessToken>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
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
            public void onNext(AccessToken accessToken) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mUserModel.saveAccessToken(accessToken);
                    mView.toMainActivity();
                    JPushHelper.setAlias(username);
                }
            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void doFirst() {
        String history = mUserModel.getHistoryUser();
        mView.setUsername(history);
    }
}
