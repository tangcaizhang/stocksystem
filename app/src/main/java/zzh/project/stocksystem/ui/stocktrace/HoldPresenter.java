package zzh.project.stocksystem.ui.stocktrace;

import java.util.List;

import zzh.project.stocksystem.bean.HoldStockBean;
import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.StockModel;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.StockModelJuheImpl;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class HoldPresenter implements HoldContract.Presenter {

    private HoldContract.View mView;
    private UserModel mUserModel;
    private StockModel mStockModel;

    public HoldPresenter(HoldContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
        mStockModel = StockModelJuheImpl.getInstance();
    }


    @Override
    public void loadHoldStockList() {
        mView.setLoadingIndicator(true);
        mUserModel.listHoldStock(new Callback2<List<HoldStockBean>, String>() {
            @Override
            public void onSuccess(List<HoldStockBean> holdStockBeen) {
                if (mView != null && mView.isActive()) {
                    mView.clearAllHoldStock();
                    final int taskCount = holdStockBeen.size();
                    if (taskCount == 0) {
                        mView.setLoadingIndicator(false);
                        return;
                    }
                    for (int i = 0; i < holdStockBeen.size(); i++) {
                        final int taskId = i;
                        final int total = holdStockBeen.get(i).total;
                        mStockModel.getDetail(holdStockBeen.get(i).gid, new Callback2<StockDetailBean, String>() {
                            @Override
                            public void onSuccess(StockDetailBean detailBean) {
                                if (mView != null && mView.isActive()) {
                                    if (taskId == taskCount - 1) {
                                        mView.setLoadingIndicator(false);
                                    }
                                    if (detailBean != null) {
                                        HoldStockBean bean = new HoldStockBean();
                                        bean.gid = detailBean.gid;
                                        bean.thumbUrl = detailBean.dayUrl;
                                        bean.increPer = detailBean.increPer;
                                        bean.increase = detailBean.increase;
                                        bean.name = detailBean.name;
                                        bean.nowPri = detailBean.nowPri;
                                        bean.total = total;
                                        mView.appendHoldStock(bean);
                                    }
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
    public void sell(int amount) {
        mView.showLoading();
        HoldStockBean selected = mView.getSelected();
        if (selected == null) {
            return;
        }
        String gid = selected.gid;
        String name = selected.name;
        float uPrice = Float.parseFloat(selected.nowPri);
        mUserModel.sell(gid, name, uPrice, amount, new Callback2<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showMessage("抛售成功");
                    mView.hideSellPop();
                    loadHoldStockList();
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

    @Override
    public void start() {
        loadHoldStockList();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
