package zzh.project.stocksystem.ui.stockdetail;


import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.StockModel;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.StockModelJuheImpl;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class StockDetailPresenter extends BasePresenter<StockDetailContract.View> implements StockDetailContract.Presenter {
    private StockModel mStockModel;
    private UserModel mUserModel;
    private boolean mGidExists;
    private StockDetailBean mStockDetail;

    StockDetailPresenter(StockDetailContract.View view) {
        super(view);
        mStockModel = StockModelJuheImpl.getInstance();
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void doFirst() {
        loadStockDetail();
        checkFavor();
    }

    @Override
    public void loadStockDetail() {
        mView.showLoading();
        mStockModel.getDetail(mView.getGid()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<StockDetailBean>() {
            @Override
            public void onCompleted() {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
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
            public void onNext(StockDetailBean detailBean) {
                mStockDetail = detailBean;
                if (mView != null && mView.isActive()) {
                    if (detailBean == null) {
                        mView.showNotFound();
                        mGidExists = false;
                    } else {
                        mView.showStockDetail(detailBean);
                        mGidExists = true;
                    }
                }
            }
        });
    }

    private void checkFavor() {
        // TODO 新增接口/缓存来检测单一
        Subscription subscription = mUserModel.listFavor().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                if (strings != null && strings.contains(mView.getGid())) {
                    mView.showAlreadyFavor();
                }
            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void favor() {
        if (!mGidExists) {
            mView.showErrorMessage("该股票不存在");
            return;
        }
        mView.showLoading();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                try {
                    mUserModel.favor(mView.getGid());
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
                    mView.showAlreadyFavor();
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

    @Override
    public void cancelFavor() {
        if (!mGidExists) {
            mView.showErrorMessage("该股票不存在");
            return;
        }
        mView.showLoading();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                try {
                    mUserModel.unFavor(mView.getGid());
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
                    mView.showUnFavor();
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

    @Override
    public void buy(final int num) {
        if (!mGidExists) {
            mView.showErrorMessage("该股票不存在");
            return;
        }
        mView.showLoading();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                try {
                    mUserModel.buy(mView.getGid(), mStockDetail.name, Float.parseFloat(mStockDetail.nowPri), num);
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
                    mView.showMessage("购买成功");
                    mView.hideBuyPop();
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
