package zzh.project.stocksystem.ui.splash;

import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface SplashContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView {
        // 跳转到主视图
        void toMainActivity();

        // 跳转到登陆视图
        void toLoginActivity();
    }
}
