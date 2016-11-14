package zzh.project.stocksystem.ui.stocktrace;

import zzh.project.stocksystem.bean.HoldStockBean;
import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface HoldContract {
    interface View extends IView {
        // 追加持有股票信息
        void appendHoldStock(HoldStockBean stockBean);

        // 清空持有股票信息
        void clearAllHoldStock();

        // 显示loading图标
        void setLoadingIndicator(boolean active);

        // 获取当前选中项
        HoldStockBean getSelected();

        // 关闭销售弹窗
        void hideSellPop();
    }

    interface Presenter extends IPresenter {
        // 加载持有股票
        void loadHoldStockList(boolean manual);

        // 卖出
        void sell(int amount);
    }
}
