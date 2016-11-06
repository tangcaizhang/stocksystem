package zzh.project.stocksystem.ui.stocktrace;

import java.util.List;

import zzh.project.stocksystem.bean.TradeBean;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class TradeListPresenter implements TradeListContract.Presenter {
    private TradeListContract.View mView;
    private UserModel mUserModel;

    public TradeListPresenter(TradeListContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void loadTradeList() {
        mView.setLoadingIndicator(true);
        mUserModel.listTrade(new Callback2<List<TradeBean>, String>() {
            @Override
            public void onSuccess(List<TradeBean> tradeBeen) {
                if (mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                    mView.showTrade(tradeBeen);
                }
            }

            @Override
            public void onError(String s) {
                if (mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                    mView.showErrorMessage(s);
                }
            }
        });
    }

    @Override
    public void start() {
        loadTradeList();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
