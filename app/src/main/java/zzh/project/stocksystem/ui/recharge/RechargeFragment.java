package zzh.project.stocksystem.ui.recharge;

import android.app.Activity;
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
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.ui.base.BaseFragment;

public class RechargeFragment extends BaseFragment<RechargeContract.Presenter> implements RechargeContract.View {
    public static final String ARGUMENT_ACCOUNT = "account";

    @BindView(R.id.tv_Recharge_CardNum)
    TextView mCardNum;
    @BindView(R.id.tv_Recharge_Money)
    TextView mMoney;
    @BindView(R.id.tv_Recharge_Pass)
    TextView mPass;

    AccountBean mAccount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = (AccountBean) getArguments().getSerializable(ARGUMENT_ACCOUNT);
    }

    @Override
    public RechargeContract.Presenter createPresenter() {
        return new RechargePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_recharge, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @OnClick(R.id.btn_Recharge)
    public void recharge() {
        if (checkInvalidate()) {
            mPresenter.recharge();
        }
    }

    private boolean checkInvalidate() {
        float money = getMoney();
        if (money <= 0) {
            showErrorMessage("请输入充值金额");
            return false;
        }
        String pass = getPassword();
        if (pass.isEmpty()) {
            showErrorMessage("请输入密码");
            return false;
        }
        return true;
    }

    private void initView() {
        mCardNum.setText(mAccount.cardNum.substring(0, 4) + "****" + mAccount.cardNum.substring(mAccount.cardNum.length() - 4));
    }

    @Override
    public String getCardNum() {
        return mAccount.cardNum;
    }

    @Override
    public String getPassword() {
        return mPass.getText().toString().trim();
    }

    @Override
    public float getMoney() {
        String moneyStr = mMoney.getText().toString().trim();
        if (moneyStr.isEmpty()) {
            return 0;
        }
        return Float.parseFloat(moneyStr);
    }

    @Override
    public void close() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
