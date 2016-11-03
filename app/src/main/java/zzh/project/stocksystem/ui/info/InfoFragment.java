package zzh.project.stocksystem.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.ui.about.AboutActivity;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.settings.SettingsActivity;

public class InfoFragment extends BaseFragment implements InfoContract.View {

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
        mPresenter.start();
        return root;
    }

    @Override
    public void onDetach() {
        mPresenter.destroy();
        super.onDetach();
    }

    @Override
    public void showUserInfo(UserBean userBean) {
        mNick.setText(userBean.nick);
        mUsername.setText(String.format("账号：%s", userBean.username));
    }

    @Override
    public void showAccount(String account) {
    }

    @Override
    public void showBalance(float balance) {

    }

    @OnClick(R.id.rl_UserInfo_AccountDetail)
    public void toAccountDetailActivity() {

    }

    @OnClick(R.id.rl_UserInfo_BalanceDetail)
    public void toBalanceDetail() {

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
