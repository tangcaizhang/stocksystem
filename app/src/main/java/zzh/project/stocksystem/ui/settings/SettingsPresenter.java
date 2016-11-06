package zzh.project.stocksystem.ui.settings;

import zzh.project.stocksystem.model.SettingsModel;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.SettingsModelImpl;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class SettingsPresenter implements SettingsContract.Presenter {
    private UserModel mUserModel;
    private SettingsContract.View mView;
    private SettingsModel mSettingModel;

    public SettingsPresenter(SettingsContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
        mSettingModel = SettingsModelImpl.getInstance();
    }

    @Override
    public void start() {
        boolean isEnable = mSettingModel.isEnablePush();
        mView.switchPush(isEnable);
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void enablePush() {
        mSettingModel.enablePush();
        mView.switchPush(true);
    }

    @Override
    public void disablePush() {
        mSettingModel.disablePush();
        mView.switchPush(false);
    }

    @Override
    public void logout() {
        mUserModel.logout();
        mView.toLoginActivity();
    }
}
