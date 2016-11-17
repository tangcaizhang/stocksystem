package zzh.project.stocksystem.ui.stocktrace;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.bean.HoldStockBean;
import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.StockManager;
import zzh.project.stocksystem.model.UserManager;
import zzh.project.stocksystem.model.impl.StockManagerJuheImpl;
import zzh.project.stocksystem.model.impl.UserManagerImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class HoldPresenter extends BasePresenter<HoldContract.View> implements HoldContract.Presenter {
    private UserManager mUserManager;
    private StockManager mStockManager;

    HoldPresenter(HoldContract.View view) {
        super(view);
        mUserManager = UserManagerImpl.getInstance();
        mStockManager = StockManagerJuheImpl.getInstance();
    }

    @Override
    public void doFirst() {
        loadHoldStockList(false);
    }

    @Override
    public void loadHoldStockList(final boolean manual) {
        if (manual) {
            mView.setLoadingIndicator(true);
        }
        mView.clearAllHoldStock();
        // 这里任务链有点复杂
        Subscription subscription = mUserManager.listHoldStock() // 拿到的HoldStockBean数据不全
                .observeOn(Schedulers.io()).flatMap(new Func1<List<HoldStockBean>, Observable<HoldStockBean>>() { // 切换到单一遍历
                    @Override
                    public Observable<HoldStockBean> call(List<HoldStockBean> holdStockBeen) {
                        return Observable.from(holdStockBeen);
                    }
                }).observeOn(Schedulers.io()).flatMap(new Func1<HoldStockBean, Observable<HoldStockBean>>() { // 转换成具有详细信息的HoldStockBean数据集
                    @Override
                    public Observable<HoldStockBean> call(final HoldStockBean holdStockBean) {
                        return mStockManager.getDetail(holdStockBean.gid)
                                .observeOn(Schedulers.io()).map(new Func1<StockDetailBean, HoldStockBean>() {
                                    @Override
                                    public HoldStockBean call(StockDetailBean detailBean) {
                                        HoldStockBean bean = new HoldStockBean();
                                        bean.gid = detailBean.gid;
                                        bean.thumbUrl = detailBean.dayUrl;
                                        bean.increPer = detailBean.increPer;
                                        bean.increase = detailBean.increase;
                                        bean.name = detailBean.name;
                                        bean.nowPri = detailBean.nowPri;
                                        bean.total = holdStockBean.total;
                                        return bean;
                                    }
                                });
                    }
                }).observeOn(Schedulers.computation()).toList() // 再重新整合为List数据源
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<HoldStockBean>>() {
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
                    public void onNext(List<HoldStockBean> holdStockBeans) {
                        if (mView != null && mView.isActive()) {
                            for (HoldStockBean holdStockBean : holdStockBeans) {
                                mView.appendHoldStock(holdStockBean);
                            }
                        }
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void sell(final int amount) {
        mView.showLoading();
        final HoldStockBean selected = mView.getSelected();
        if (selected == null) {
            return;
        }
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                String gid = selected.gid;
                String name = selected.name;
                float uPrice = Float.parseFloat(selected.nowPri);
                try {
                    mUserManager.sell(gid, name, uPrice, amount);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showMessage("抛售成功");
                    mView.hideSellPop();
                    loadHoldStockList(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showErrorMessage(MsgHelper.getErrorMsg(e));
                }
            }

            @Override
            public void onNext(Void aVoid) {

            }
        });
        mSubscription.add(subscription);
    }
}
