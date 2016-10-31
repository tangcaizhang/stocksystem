package zzh.project.stocksystem.ui.register;

import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface RegisterContract {
    interface View extends BaseView {
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

    interface Presenter extends BasePresenter {
        // 注册
        void register();
    }
}
