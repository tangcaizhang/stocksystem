package zzh.project.stocksystem.ui.stockdetail;

import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface StockDetailContract {
    interface View extends IView {
        // 显示股票信息
        void showStockDetail(StockDetailBean detail);

        // 显示无法找到
        void showNotFound();

        // 获取股票代号
        String getGid();

        // 显示未关注
        void showUnFavor();

        // 显示关注
        void showAlreadyFavor();

        // 关闭购买弹窗
        void hideBuyPop();
    }

    interface Presenter extends IPresenter {
        // 加载股票信息
        void loadStockDetail();

        // 关注
        void favor();

        // 取消关注
        void cancelFavor();

        // 购买
        void buy(int num);
    }
}
