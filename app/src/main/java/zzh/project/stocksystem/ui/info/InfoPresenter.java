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
        mUserModel.getInfo(new Callback2<UserBean, String>() {
            @Override
            public void onSuccess(UserBean userBean) {
                if (mView != null && mView.isActive()) {
                    mView.showUserInfo(userBean);
                }
            }

            @Override
            public void onError(String s) {
                if (mView != null && mView.isActive()) {
                    mView.showErrorMessage(s);
                }
            }
        });
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
