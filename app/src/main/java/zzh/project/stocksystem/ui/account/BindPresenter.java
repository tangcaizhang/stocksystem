package zzh.project.stocksystem.ui.account;

import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.util.Md5Util;

public class BindPresenter implements BindContract.Presenter {

    private BindContract.View mView;
    private UserModel mUserModel;

    public BindPresenter(BindContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void bindAccount() {
        mView.showLoading();
        String cardNum = mView.getCardNum();
        String realName = mView.getRealName();
        String idNum = mView.getIdNum();
        String pass = mView.getPassword();
        AccountBean accountBean = new AccountBean();
        accountBean.cardNum = cardNum;
        accountBean.idNum = idNum;
        accountBean.realName = realName;
        accountBean.password = Md5Util.toMD5(pass);
        mUserModel.bindAccount(accountBean, new Callback2<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mView != null && mView.isActive()) {
                    mView.hideLoading();
                    mView.showMessage("绑定成功");
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
