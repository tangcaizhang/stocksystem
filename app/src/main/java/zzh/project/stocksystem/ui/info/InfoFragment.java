package zzh.project.stocksystem.ui.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.ui.about.AboutActivity;
import zzh.project.stocksystem.ui.account.AccountDetailActivity;
import zzh.project.stocksystem.ui.account.BindActivity;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.recharge.RechargeActivity;
import zzh.project.stocksystem.ui.settings.SettingsActivity;
import zzh.project.stocksystem.widget.ScrollChildSwipeRefreshLayout;

public class InfoFragment extends BaseFragment implements InfoContract.View {
    public static final int REQUEST_CODE_BIND = 0;
    public static final int REQUEST_CODE_RECHARGE = 1;

    @BindView(R.id.rl_UserInfo_Refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_UserInfo_Username)
    TextView mUsername;
    @BindView(R.id.tv_UserInfo_Nick)
    TextView mNick;
    @BindView(R.id.tv_UserInfo_Account)
    TextView mAccount;
    @BindView(R.id.tv_UserInfo_Balance)
    TextView mBalance;


    private InfoPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new InfoPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_info, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void initView() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadUserInfo();
                mPresenter.loadAccount();
            }
        });
    }

    @Override
    public void onDetach() {
        mPresenter.destroy();
        super.onDetach();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showUserInfo(UserBean userBean) {
        mNick.setText(userBean.nick);
        mUsername.setText(String.format("账号：%s", userBean.username));
        showBalance(userBean.balance);
    }

    @Override
    public void showAccount(String carNum) {
        mAccount.setText(String.format("支付账号：%s", TextUtils.isEmpty(carNum) ? "尚未绑定" :
                carNum.substring(0, 4) + "****" + carNum.substring(carNum.length() - 4)));
    }

    @Override
    public void showBalance(float balance) {
        mBalance.setText(String.format("余额：%s", balance));
    }

    @Override
    public void toReChargeActivity(AccountBean accountBean) {
        Intent intent = new Intent(getContext(), RechargeActivity.class);
        intent.putExtra(RechargeActivity.ARGUMENT_ACCOUNT, accountBean);
        startActivityForResult(intent, REQUEST_CODE_RECHARGE);
    }

    @Override
    public void toBindAccountActivity() {
        Intent intent = new Intent(getContext(), BindActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BIND);
    }

    @Override
    public void toAccountDetailActivity(AccountBean accountBean) {
        Intent intent = new Intent(getContext(), AccountDetailActivity.class);
        intent.putExtra(AccountDetailActivity.ARGUMENT_ACCOUNT, accountBean);
        startActivity(intent);
    }

    @OnClick(R.id.rl_UserInfo_AccountDetail)
    public void toBindAccount() {
        mPresenter.bindAccount();
    }

    @OnClick(R.id.rl_UserInfo_Recharge)
    public void toReCharge() {
        mPresenter.recharge();
    }

    @OnClick(R.id.rl_UserInfo_Settings)
    public void toSettingsActivity() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_UserInfo_About)
    public void toAboutActivity() {
        Intent intent = new Intent(getContext(), AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BIND && resultCode == Activity.RESULT_OK) {
            mPresenter.loadAccount();
        }
        if (requestCode == REQUEST_CODE_RECHARGE && resultCode == Activity.RESULT_OK) {
            mPresenter.loadUserInfo();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
