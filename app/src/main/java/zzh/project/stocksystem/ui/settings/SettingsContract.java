package zzh.project.stocksystem.ui.settings;

import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface SettingsContract {
    interface View extends BaseView {
        // 切换推送提示开关
        void switchPush(boolean enable);

        // 跳转到登陆界面
        void toLoginActivity();
    }

    interface Presenter extends BasePresenter {
        // 启用推送
        void enablePush();

        // 关闭推送
        void disablePush();

        // 登出
        void logout();
    }
}
