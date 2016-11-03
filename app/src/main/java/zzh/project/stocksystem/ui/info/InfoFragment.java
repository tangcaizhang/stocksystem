package zzh.project.stocksystem.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.settings.SettingsActivity;
import zzh.project.stocksystem.widget.ScrollChildSwipeRefreshLayout;

public class InfoFragment extends BaseFragment implements InfoContract.View {

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
        mPresenter.start();
        return root;
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
    public void showAccount(AccountBean account) {
        mAccount.setText(String.format("支付账号：%s", account.carNum));
    }

    @Override
    public void showBalance(float balance) {
        mBalance.setText(String.format("余额：%s", balance));
    }

    @Override
    public void toReChargeActivity() {

    }

    @Override
    public void toBindAccountActivity() {

    }

    @Override
    public void toAccountDetailActivity() {

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
}
