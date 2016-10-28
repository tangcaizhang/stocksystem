package zzh.project.stocksystem.module.register;

import zzh.project.stocksystem.module.base.BasePresenter;
import zzh.project.stocksystem.module.base.BaseView;

public interface RegisterContract {
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
