package zzh.project.stocksystem.ui.stocktrace;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.bean.TradeBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class TradeListPresenter extends BasePresenter<TradeListContract.View> implements TradeListContract.Presenter {
    private UserModel mUserModel;

    TradeListPresenter(TradeListContract.View view) {
        super(view);
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void doFirst() {
        loadTradeList(false);
    }

    @Override
    public void loadTradeList(final boolean manual) {
        if (manual) {
            mView.setLoadingIndicator(true);
        }
        mSubscription.clear();
        Subscription subscription = mUserModel.listTrade().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<TradeBean>>() {
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
            public void onNext(List<TradeBean> tradeBeen) {
                if (mView != null && mView.isActive()) {
                    mView.showTrade(tradeBeen);
                }
            }
        });
        mSubscription.add(subscription);
    }
}
