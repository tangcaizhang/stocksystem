package zzh.project.stocksystem.ui.info;

import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class InfoPresenter implements InfoContract.Presenter {
    private UserModel mUserModel;
    private InfoContract.View mView;
    private AccountBean mAccountBean;

    public InfoPresenter(InfoContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void loadUserInfo() {
        mView.setLoadingIndicator(true);
        mUserModel.getInfo(new Callback2<UserBean, String>() {
            @Override
            public void onSuccess(UserBean userBean) {
                mView.setLoadingIndicator(false);
                if (mView != null && mView.isActive()) {
                    mView.showUserInfo(userBean);
                }
            }

            @Override
            public void onError(String s) {
                mView.setLoadingIndicator(false);
                if (mView != null && mView.isActive()) {
                    mView.showErrorMessage(s);
                }
            }
        });
    }

    @Override
    public void loadAccount() {
        mUserModel.getAccount(new Callback2<AccountBean, String>() {
            @Override
            public void onSuccess(AccountBean accountBean) {
                if (mView != null && mView.isActive()) {
                    mAccountBean = accountBean;
                    mView.showAccount(accountBean == null ? null : accountBean.cardNum);
                }
            }

            @Override
            public void onError(String s) {
                if (mView != null && mView.isActive()) {
                    mView.showAccount(null);
                }
            }
        });
    }

    @Override
    public void recharge() {
        if (mAccountBean == null) {
            mView.showErrorMessage("尚未绑定支付账号");
        } else {
            mView.toReChargeActivity(mAccountBean);
        }
    }

    @Override
    public void bindAccount() {
        if (mAccountBean == null) {
            mView.toBindAccountActivity();
        } else {
            mView.toAccountDetailActivity(mAccountBean);
        }
    }

    @Override
    public void start() {
        loadUserInfo();
        loadAccount();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
