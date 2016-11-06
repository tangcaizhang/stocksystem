package zzh.project.stocksystem.ui.account;

import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface BindContract {
    interface View extends BaseView {
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

    interface Presenter extends BasePresenter {
        // 绑定卡片
        void bindAccount();
    }
}
