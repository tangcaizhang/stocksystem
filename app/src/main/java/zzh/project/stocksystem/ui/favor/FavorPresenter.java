package zzh.project.stocksystem.ui.favor;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.StockManager;
import zzh.project.stocksystem.model.UserManager;
import zzh.project.stocksystem.model.impl.StockManagerJuheImpl;
import zzh.project.stocksystem.model.impl.UserManagerImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class FavorPresenter extends BasePresenter<FavorContract.View> implements FavorContract.Presenter {
    private UserManager mUserManager;
    private StockManager mStockManager;

    FavorPresenter(FavorContract.View view) {
        super(view);
        mUserManager = UserManagerImpl.getInstance();
        mStockManager = StockManagerJuheImpl.getInstance();
    }

    @Override
    public void doFirst() {
        loadFavorList(false);
    }

    @Override
    public void loadFavorList(final boolean manual) {
        if (manual) {
            mView.setLoadingIndicator(true);
        }
        mView.clearAllFavorStock();
        mSubscription.clear();
        Subscription subscription = mUserManager.listFavor().observeOn(Schedulers.computation())
                .flatMap(new Func1<List<String>, Observable<StockDetailBean>>() {
                    @Override
                    public Observable<StockDetailBean> call(List<String> strings) {
                        return Observable.from(strings).flatMap(new Func1<String, Observable<StockDetailBean>>() {
                            @Override
                            public Observable<StockDetailBean> call(String s) {
                                return mStockManager.getDetail(s);
                            }
                        });
                    }
                }).map(new Func1<StockDetailBean, StockBean>() {
                    @Override
                    public StockBean call(StockDetailBean detailBean) {
                        StockBean bean = new StockBean();
                        bean.gid = detailBean.gid;
                        bean.thumbUrl = detailBean.dayUrl;
                        bean.increPer = detailBean.increPer;
                        bean.increase = detailBean.increase;
                        bean.name = detailBean.name;
                        bean.nowPri = detailBean.nowPri;
                        return bean;
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<StockBean>() {
                    @Override
                    public void onCompleted() {
                        if (manual && mView != null && mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null && mView.isActive()) {
                            if (manual) {
                                mView.setLoadingIndicator(false);
                            }
                            mView.showErrorMessage(MsgHelper.getErrorMsg(e));
                        }
                    }

                    @Override
                    public void onNext(StockBean stockBean) {
                        if (mView != null && mView.isActive()) {
                            mView.appendFavorStock(stockBean);
                        }
                    }
                });
        mSubscription.add(subscription);
    }
}
