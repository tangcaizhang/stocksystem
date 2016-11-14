package zzh.project.stocksystem.ui.login;

import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface LoginContract {
    interface View extends IView {
        // 获取用户名
        String getUsername();

        // 获取密码
        String getPassword();

        // 清空用户名
        void clearUsername();

        // 清空密码
        void clearPassword();

        // 设置用户名
        void setUsername(String username);

        void toMainActivity();
    }

    interface Presenter extends IPresenter {
        // 登陆
        void login();
    }
}
