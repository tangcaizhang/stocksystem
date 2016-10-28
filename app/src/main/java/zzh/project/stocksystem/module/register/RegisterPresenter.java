package zzh.project.stocksystem.module.register;

import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.model.IUserModel;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.util.Md5Util;

public class RegisterPresenter implements RegisterContract.Presenter {
    public static final String TAG = RegisterPresenter.class.getSimpleName();
    private RegisterContract.View mView;
    private UserModel mUserModel;

    public RegisterPresenter(RegisterContract.View view) {
        mView = view;
        mUserModel = UserModel.getInstance();
    }

    @Override
    public void register() {
        UserBean bean = new UserBean();
        bean.username = mView.getUsername().trim();
        bean.nick = mView.getNick().trim();
        bean.email = mView.getEmail().trim();
        bean.password = Md5Util.toMD5(mView.getPassword().trim());
        mView.showLoading();
        mUserModel.register(bean, new IUserModel.Callback<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showMessage("注册成功");
                    mView.close();
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

    }

    @Override
    public void destroy() {
        mView = null;
    }
}
