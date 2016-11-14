package zzh.project.stocksystem.ui.stock;

import java.util.List;

import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockListType;
import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface StockListContract {
    interface View extends IView {
        // 显示股票数据
        void showStocks(List<StockBean> stocks);

        // 显示追加股票数据
        void appendStocks(List<StockBean> stocks);

        // 显示loading图标
        void setLoadingIndicator(boolean active);

        // 获取股票列表类型
        StockListType getType();
    }

    interface Presenter extends IPresenter {
        // 加载股票数据
        void loadStocks(boolean manual);

        // 加载更多
        void loadMore();
    }
}
