package zzh.project.stocksystem.ui.recharge;

import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface RechargeContract {
    interface View extends IView {
        // 获取用户名
        String getCardNum();

        // 获取密码
        String getPassword();

        // 获取昵称
        float getMoney();

        void close();
    }

    interface Presenter extends IPresenter {
        // 充值
        void recharge();
    }
}
