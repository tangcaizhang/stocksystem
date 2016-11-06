package zzh.project.stocksystem.ui.info;

import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface InfoContract {
    interface View extends BaseView {
        // 显示loading图标
        void setLoadingIndicator(boolean active);

        // 显示用户信息
        void showUserInfo(UserBean userBean);

        // 显示交易账号
        void showAccount(String cardNum);

        // 显示余额
        void showBalance(float balance);

        // 跳转到充值
        void toReChargeActivity(AccountBean accountBean);

        // 跳转到绑定支付
        void toBindAccountActivity();

        // 跳转到支付账号详情
        void toAccountDetailActivity(AccountBean accountBean);
    }

    interface Presenter extends BasePresenter {
        // 加载用户信息
        void loadUserInfo();

        // 加载卡号详情
        void loadAccount();

        // 充值
        void recharge();

        // 绑定支付账号
        void bindAccount();
    }
}
