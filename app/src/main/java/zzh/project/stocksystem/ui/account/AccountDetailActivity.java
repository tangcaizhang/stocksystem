package zzh.project.stocksystem.ui.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.ui.base.BaseActivity;

public class AccountDetailActivity extends BaseActivity {
    public static final String ARGUMENT_ACCOUNT = "account";

    @BindView(R.id.tv_AccountDetail_CardNum)
    TextView mCardNum;
    @BindView(R.id.tv_AccountDetail_RealName)
    TextView mRealName;
    @BindView(R.id.tv_AccountDetail_IdNum)
    TextView mIdNum;

    private AccountBean mAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_account_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_black);
        ab.setDisplayHomeAsUpEnabled(true);


        ButterKnife.bind(this);

        if (getIntent() != null) {
            mAccount = (AccountBean) getIntent().getSerializableExtra(ARGUMENT_ACCOUNT);
            initView();
        }
    }

    private void initView() {
        mCardNum.setText(mAccount.cardNum.substring(0, 4) + "****" + mAccount.cardNum.substring(mAccount.cardNum.length() - 4));
        mRealName.setText(mAccount.realName.substring(0, 1) + "**" + mAccount.realName.substring(mAccount.realName.length() - 1));
        mIdNum.setText(mAccount.idNum.substring(0, 4) + "****" + mAccount.idNum.substring(mAccount.idNum.length() - 4));
    }

    @OnClick(R.id.btn_AccountDetail_Confirm)
    public void close() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onContextItemSelected(item);
    }
}
