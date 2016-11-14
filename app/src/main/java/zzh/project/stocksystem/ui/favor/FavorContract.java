package zzh.project.stocksystem.ui.favor;

import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface FavorContract {
    interface View extends IView {
        // 追加股票
        void appendFavorStock(StockBean stockBean);

        // 清空关注
        void clearAllFavorStock();

        // 显示loading图标
        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends IPresenter {
        // 加载关注列表
        void loadFavorList(boolean manual);
    }
}
