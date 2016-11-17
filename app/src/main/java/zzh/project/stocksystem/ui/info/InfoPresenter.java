package zzh.project.stocksystem.ui.info;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.UserManager;
import zzh.project.stocksystem.model.exception.StockSystemException;
import zzh.project.stocksystem.model.impl.UserManagerImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

class InfoPresenter extends BasePresenter<InfoContract.View> implements InfoContract.Presenter {
    private UserManager mUserManager;
    private AccountBean mAccountBean;

    InfoPresenter(InfoContract.View view) {
        super(view);
        mUserManager = UserManagerImpl.getInstance();
    }

    @Override
    public void loadUserInfo(final boolean manual) {
        if (manual) {
            mView.setLoadingIndicator(true);
        }
        Subscription subscription = mUserManager.getInfo().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserBean>() {
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
            public void onNext(UserBean userBean) {
                if (mView != null && mView.isActive()) {
                    mView.showUserInfo(userBean);
                }
            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void loadAccount(final boolean manual) {
        if (manual) {
            mView.setLoadingIndicator(true);
        }
        Subscription subscription = mUserManager.getAccount().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AccountBean>() {
            @Override
            public void onCompleted() {
                if (manual && mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof StockSystemException) {
                    mAccountBean = null;
                }
                if (mView != null && mView.isActive()) {
                    if (manual) {
                        mView.setLoadingIndicator(false);
                    }
                    mView.showAccount(null);
                }
            }

            @Override
            public void onNext(AccountBean accountBean) {
                mAccountBean = accountBean;
                if (mView != null && mView.isActive()) {
                    mView.showAccount(accountBean == null ? null : accountBean.cardNum);
                }
            }
        });
        mSubscription.add(subscription);
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
    public void doFirst() {
        loadUserInfo(false);
        loadAccount(false);
    }
}
