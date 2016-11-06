package zzh.project.stocksystem.ui.stockdetail;


import java.util.List;

import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.StockModel;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.StockModelJuheImpl;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class StockDetailPresenter implements StockDetailContract.Presenter {
    private StockDetailContract.View mView;
    private StockModel mStockModel;
    private UserModel mUserModel;
    private boolean mGidExists;
    private StockDetailBean mStockDetail;

    public StockDetailPresenter(StockDetailContract.View view) {
        mView = view;
        mStockModel = StockModelJuheImpl.getInstance();
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void start() {
        loadStockDetail();
        checkFavor();
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
                mStockDetail = detailBean;
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    if (detailBean == null) {
                        mView.showNotFound();
                        mGidExists = false;
                    } else {
                        mView.showStockDetail(detailBean);
                        mGidExists = true;
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

    private void checkFavor() {
        // TODO 新增接口/缓存
        mUserModel.listFavor(new Callback2<List<String>, String>() {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings.contains(mView.getGid())) {
                    mView.showAlreadyFavor();
                }
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    @Override
    public void favor() {
        if (!mGidExists) {
            mView.showErrorMessage("该股票不存在");
            return;
        }

        mView.showLoading();
        mUserModel.favor(mView.getGid(), new Callback2<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showAlreadyFavor();
                }
            }

            @Override
            public void onError(String s) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showErrorMessage(s);
                    mView.showUnFavor();
                }
            }
        });
    }

    @Override
    public void cancelFavor() {
        if (!mGidExists) {
            mView.showErrorMessage("该股票不存在");
            return;
        }

        mView.showLoading();
        mUserModel.unFavor(mView.getGid(), new Callback2<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showUnFavor();
                }
            }

            @Override
            public void onError(String s) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showErrorMessage(s);
                    mView.showAlreadyFavor();
                }
            }
        });
    }

    @Override
    public void buy(int num) {
        if (!mGidExists) {
            mView.showErrorMessage("该股票不存在");
            return;
        }
        mView.showLoading();
        mUserModel.buy(mView.getGid(), mStockDetail.name, Float.parseFloat(mStockDetail.nowPri), num, new Callback2<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showMessage("购买成功");
                    mView.hideBuyPop();
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
