package zzh.project.stocksystem.ui.splash;

import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface SplashContract {
    interface Presenter extends IPresenter {
        // 执行初始化
        void doInit();
    }

    interface View extends IView {
        // 跳转到主视图
        void toMainActivity();

        // 跳转到登陆视图
        void toLoginActivity();
    }
}
