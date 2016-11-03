package zzh.project.stocksystem.ui.info;

import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class InfoPresenter implements InfoContract.Presenter {
    private UserModel mUserModel;
    private InfoContract.View mView;

    public InfoPresenter(InfoContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void loadUserInfo() {
        mView.setLoadingIndicator(true);
        mUserModel.getInfo(new Callback2<UserBean, String>() {
            @Override
            public void onSuccess(UserBean userBean) {
                mView.setLoadingIndicator(false);
                if (mView != null && mView.isActive()) {
                    mView.showUserInfo(userBean);
                }
            }

            @Override
            public void onError(String s) {
                mView.setLoadingIndicator(false);
                if (mView != null && mView.isActive()) {
                    mView.showErrorMessage(s);
                }
            }
        });
    }

    @Override
    public void loadAccount() {

    }

    @Override
    public void recharge() {

    }

    @Override
    public void bindAccount() {

    }

    @Override
    public void start() {
        loadUserInfo();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
