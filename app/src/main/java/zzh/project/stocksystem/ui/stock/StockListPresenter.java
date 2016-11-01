package zzh.project.stocksystem.ui.stock;

import android.util.Log;

import java.util.List;

import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockListType;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.StockModel;
import zzh.project.stocksystem.model.impl.StockModelJuheImpl;

public class StockListPresenter implements StockListContract.Presenter {
    public static final String TAG = StockListPresenter.class.getSimpleName();
    private StockListContract.View mView;
    private StockModel mStockModel;
    private int mCurPage = 0;

    public StockListPresenter(StockListContract.View view) {
        mView = view;
        mStockModel = StockModelJuheImpl.getInstance();
    }

    @Override
    public void loadStocks() {
        mCurPage = 0;
        Callback2<List<StockBean>, String> callback = new Callback2<List<StockBean>, String>() {
            @Override
            public void onSuccess(List<StockBean> stockBeen) {
                if (mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                    mView.showStocks(stockBeen);
                }
            }

            @Override
            public void onError(String s) {
                if (mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                    mView.showErrorMessage(s);
                }
            }
        };
        loadStocks(mCurPage, callback);
    }

    @Override
    public void loadMore() {
        mCurPage++;
        Callback2<List<StockBean>, String> callback = new Callback2<List<StockBean>, String>() {
            @Override
            public void onSuccess(List<StockBean> stockBeen) {
                if (mView != null && mView.isActive()) {
                    mView.appendStocks(stockBeen);
                }
            }

            @Override
            public void onError(String s) {
                mView.appendStocks(null);
                mCurPage--;
            }
        };
        loadStocks(mCurPage, callback);
    }

    private void loadStocks(int page, Callback2<List<StockBean>, String> callback) {
        if (mView.getType() == StockListType.SH) {
            mStockModel.findAllSH(page, callback);
        } else if (mView.getType() == StockListType.SZ) {
            mStockModel.findAllSZ(page, callback);
        } else if (mView.getType() == StockListType.HK) {
            mStockModel.findAllHK(page, callback);
        } else if (mView.getType() == StockListType.USA) {
            mStockModel.findAllUS(page, callback);
        }
    }

    @Override
    public void start() {
        loadStocks();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
