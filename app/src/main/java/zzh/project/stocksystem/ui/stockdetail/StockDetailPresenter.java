package zzh.project.stocksystem.ui.stockdetail;


import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.StockModel;
import zzh.project.stocksystem.model.impl.StockModelJuheImpl;

public class StockDetailPresenter implements StockDetailContract.Presenter {
    private StockDetailContract.View mView;
    private StockModel mStockModel;

    public StockDetailPresenter(StockDetailContract.View view) {
        mView = view;
        mStockModel = StockModelJuheImpl.getInstance();
    }

    @Override
    public void start() {
        loadStockDetail();
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void loadStockDetail() {
        mView.showLoading();
        mStockModel.getDetail(mView.getGid(), new Callback2<StockDetailBean, String>() {
            @Override
            public void onSuccess(StockDetailBean detailBean) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    if (detailBean == null) {
                        mView.showNotFound();
                    } else {
                        mView.showStockDetail(detailBean);
                    }
                }
            }

            @Override
            public void onError(String s) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showErrorMessage(s);
                }
            }
        });
    }
}
