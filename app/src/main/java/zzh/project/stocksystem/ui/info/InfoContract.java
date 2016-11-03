package zzh.project.stocksystem.ui.info;

import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface InfoContract {
    interface View extends BaseView {
        // 显示用户信息
        void showUserInfo(UserBean userBean);

        // 显示交易账号
        void showAccount(String account);

        // 显示余额
        void showBalance(float balance);
    }

    interface Presenter extends BasePresenter {
        // 加载用户信息
        void loadUserInfo();
        
    }
}
