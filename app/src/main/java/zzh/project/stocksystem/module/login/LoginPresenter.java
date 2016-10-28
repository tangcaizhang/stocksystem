package zzh.project.stocksystem.module.login;

import zzh.project.stocksystem.domain.AccessToken;
import zzh.project.stocksystem.model.IUserModel;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.util.Md5Util;

public class LoginPresenter implements LoginContract.Presenter {
    public static final String TAG = LoginPresenter.class.getSimpleName();
    private LoginContract.View mView;
    private UserModel mUserModel;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mUserModel = UserModel.getInstance();
    }

    @Override
    public void login() {
        final String username = mView.getUsername().trim();
        String password = mView.getPassword().trim();
        password = Md5Util.toMD5(password);
        mUserModel.setHistoryUser(username);
        mView.showLoading();
        mUserModel.login(username, password, new IUserModel.Callback<AccessToken, String>() {
            @Override
            public void onSuccess(AccessToken accessToken) {
                mUserModel.saveAccessToken(accessToken);
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.toMainActivity();
                }
            }

            @Override
            public void onError(String errMsg) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showErrorMessage(errMsg);
                }
            }
        });
    }

    @Override
    public void start() {
        String history = mUserModel.getHistoryUser();
        mView.setUsername(history);
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
