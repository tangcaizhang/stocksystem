package zzh.project.stocksystem.ui.recharge;

import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.util.Md5Util;

public class RechargePresenter implements RechargeContract.Presenter {

    private RechargeContract.View mView;
    private UserModel mUserModel;

    public RechargePresenter(RechargeContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void recharge() {
        mView.showLoading();
        String cardNum = mView.getCardNum();
        String pass = Md5Util.toMD5(mView.getPassword());
        float money = mView.getMoney();
        mUserModel.recharge(cardNum, pass, money, new Callback2<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showMessage("充值成功");
                    mView.close();
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

    }

    @Override
    public void destroy() {
        mView = null;
    }
}
