package zzh.project.stocksystem.ui.favor;

import java.util.List;

import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.StockModel;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.StockModelJuheImpl;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class FavorPresenter implements FavorContract.Presenter {
    private FavorContract.View mView;
    private UserModel mUserModel;
    private StockModel mStockModel;

    public FavorPresenter(FavorContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
        mStockModel = StockModelJuheImpl.getInstance();
    }


    @Override
    public void loadFavorList() {
        mView.setLoadingIndicator(true);
        mUserModel.listFavor(new Callback2<List<String>, String>() {
            @Override
            public void onSuccess(List<String> gids) {
                mView.clearAllFavorStock();
                final int taskCount = gids.size();
                if (taskCount == 0) {
                    mView.setLoadingIndicator(false);
                    return;
                }
                for (int i = 0; i < gids.size(); i++) {
                    final int taskId = i;
                    mStockModel.getDetail(gids.get(i), new Callback2<StockDetailBean, String>() {
                        @Override
                        public void onSuccess(StockDetailBean detailBean) {
                            if (mView != null && mView.isActive()) {
                                if (taskId == taskCount - 1) {
                                    mView.setLoadingIndicator(false);
                                }
                                if (detailBean != null) {
                                    StockBean bean = new StockBean();
                                    bean.gid = detailBean.gid;
                                    bean.thumbUrl = detailBean.dayUrl;
                                    bean.increPer = detailBean.increPer;
                                    bean.increase = detailBean.increase;
                                    bean.name = detailBean.name;
                                    bean.nowPri = detailBean.nowPri;
                                    mView.appendFavorStock(bean);
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
        loadFavorList();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
