package zzh.project.stocksystem.ui.stock;

import java.util.List;

import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockListType;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface StockListContract {
    interface Presenter extends BasePresenter {
        // 加载股票数据
        void loadStocks();

        // 加载更多
        void loadMore();
    }

    interface View extends BaseView {
        // 显示股票数据
        void showStocks(List<StockBean> stocks);

        // 显示追加股票数据
        void appendStocks(List<StockBean> stocks);

        // 显示loading图标
        void setLoadingIndicator(boolean active);

        // 获取股票列表类型
        StockListType getType();
    }
}
