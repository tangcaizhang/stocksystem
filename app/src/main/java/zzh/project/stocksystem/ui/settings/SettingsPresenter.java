package zzh.project.stocksystem.ui.settings;

import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class SettingsPresenter implements SettingsContract.Presenter {
    private UserModel mUserModel;
    private SettingsContract.View mView;

    public SettingsPresenter(SettingsContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void start() {
        // TODO 利用model读取SP，还原上次配置
        enablePush();
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void enablePush() {
        mView.switchPush(true);
    }

    @Override
    public void disablePush() {
        mView.switchPush(false);
    }

    @Override
    public void logout() {
        mUserModel.logout();
        mView.toLoginActivity();
    }
}
