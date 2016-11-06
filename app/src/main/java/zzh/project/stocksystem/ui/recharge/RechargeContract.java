package zzh.project.stocksystem.ui.recharge;

import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface RechargeContract {
    interface View extends BaseView {
        // 获取用户名
        String getCardNum();

        // 获取密码
        String getPassword();

        // 获取昵称
        float getMoney();

        void close();
    }

    interface Presenter extends BasePresenter {
        // 充值
        void recharge();
    }
}
