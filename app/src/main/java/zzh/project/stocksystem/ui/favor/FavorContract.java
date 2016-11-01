package zzh.project.stocksystem.ui.favor;

import java.util.List;

import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface FavorContract {
    interface View extends BaseView {
        // 显示关注的股票列表
        void showFavorStock(List<StockBean> stockBeen);

        // 追加股票
        void appendFavorStock(StockBean stockBean);

        // 清空关注
        void clearAllFavorStock();

        // 显示loading图标
        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {
        // 加载关注列表
        void loadFavorList();
    }
}
