package zzh.project.stocksystem.ui.stocktrace;

import java.util.List;

import zzh.project.stocksystem.bean.TradeBean;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface TradeListContract {
    interface View extends BaseView {
        // 清空交易记录
        void clearAllTrade();

        // 显示交易记录
        void showTrade(List<TradeBean> trades);

        // 显示loading图标
        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {
        // 加载交易记录
        void loadTradeList();
    }
}
