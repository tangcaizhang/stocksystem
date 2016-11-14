package zzh.project.stocksystem.ui.settings;

import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface SettingsContract {
    interface View extends IView {
        // 切换推送提示开关
        void switchPush(boolean enable);

        // 跳转到登陆界面
        void toLoginActivity();
    }

    interface Presenter extends IPresenter {
        // 启用推送
        void enablePush();

        // 关闭推送
        void disablePush();

        // 登出
        void logout();
    }
}
