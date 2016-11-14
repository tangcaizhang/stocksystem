package zzh.project.stocksystem.ui.register;

import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface RegisterContract {
    interface View extends IView {
        // 获取用户名
        String getUsername();

        // 获取邮箱
        String getEmail();

        // 获取密码
        String getPassword();

        // 获取昵称
        String getNick();

        void close();
    }

    interface Presenter extends IPresenter {
        // 注册
        void register();
    }
}
