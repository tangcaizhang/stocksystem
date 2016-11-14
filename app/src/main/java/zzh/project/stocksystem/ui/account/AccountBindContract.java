package zzh.project.stocksystem.ui.account;

import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface AccountBindContract {
    interface View extends IView {
        // 获取卡号
        String getCardNum();

        // 获取真实姓名
        String getRealName();

        // 获取证件号码
        String getIdNum();

        // 获取密码
        String getPassword();

        // 关闭
        void close();
    }

    interface Presenter extends IPresenter {
        // 绑定卡片
        void bindAccount();
    }
}
