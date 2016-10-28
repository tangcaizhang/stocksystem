package zzh.project.stocksystem.module.splash;

import zzh.project.stocksystem.module.base.BasePresenter;
import zzh.project.stocksystem.module.base.BaseView;

public interface SplashContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView {
        // 跳转到主视图
        void toMainActivity();

        // 跳转到登陆视图
        void toLoginActivity();
    }
}
