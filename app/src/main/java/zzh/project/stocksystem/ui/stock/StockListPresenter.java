package zzh.project.stocksystem.ui.stock;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockListType;
import zzh.project.stocksystem.model.StockManager;
import zzh.project.stocksystem.model.impl.StockManagerJuheImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class StockListPresenter extends BasePresenter<StockListContract.View> implements StockListContract.Presenter {
    private StockManager mStockManager;
    private int mCurPage = 0;

    StockListPresenter(StockListContract.View view) {
        super(view);
        mStockManager = StockManagerJuheImpl.getInstance();
    }

    @Override
    public void doFirst() {
        loadStocks(false);
    }

    @Override
    public void loadStocks(boolean manual) {
        if (manual) {
            mView.setLoadingIndicator(true);
        }
        mCurPage = 0;
        loadStocks(0, new Subscriber<List<StockBean>>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onNext(List<StockBean> stockBeen) {
                if (mView != null && mView.isActive()) {
                    mView.showStocks(stockBeen);
                }
            }
        });
    }

    @Override
    public void loadMore() {
        mCurPage++;
        loadStocks(mCurPage, new Subscriber<List<StockBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (mView != null && mView.isActive()) {
                    mView.appendStocks(null);
                    mCurPage--;
                }
            }

            @Override
            public void onNext(List<StockBean> stockBeen) {
                if (mView != null && mView.isActive()) {
                    mView.appendStocks(stockBeen);
                }
            }
        });
    }

    private void loadStocks(int page, Subscriber<List<StockBean>> subscriber) {
        Subscription subscription = null;
        if (mView.getType() == StockListType.SH) {
            subscription = mStockManager.findAllSH(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        } else if (mView.getType() == StockListType.SZ) {
            subscription = mStockManager.findAllSZ(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        } else if (mView.getType() == StockListType.HK) {
            subscription = mStockManager.findAllHK(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        } else if (mView.getType() == StockListType.USA) {
            subscription = mStockManager.findAllUS(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        if (subscription != null) {
            mSubscription.add(subscription);
        }
    }
}
