package zzh.project.stocksystem.ui.account;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.ui.base.BaseFragment;

public class AccountBindFragment extends BaseFragment<AccountBindContract.Presenter> implements AccountBindContract.View {
    @BindView(R.id.til_BindCard_CardNum_Wrapper)
    TextInputLayout mCardNumWrapper;
    @BindView(R.id.et_BindCard_CardNum)
    EditText mCardNum;
    @BindView(R.id.til_BindCard_RealName_Wrapper)
    TextInputLayout mRealNameWrapper;
    @BindView(R.id.et_BindCard_RealName)
    EditText mRealName;
    @BindView(R.id.til_BindCard_IdNum_Wrapper)
    TextInputLayout mIdNumWrapper;
    @BindView(R.id.et_BindCard_IdNum)
    EditText mIdNum;
    @BindView(R.id.til_BindCard_Pass_Wrapper)
    TextInputLayout mPassWrapper;
    @BindView(R.id.et_BindCard_Pass)
    EditText mPass;
    @BindView(R.id.til_BindCard_RePass_Wrapper)
    TextInputLayout mRePassWrapper;
    @BindView(R.id.et_BindCard_RePass)
    EditText mRePass;

    @Override
    public AccountBindContract.Presenter createPresenter() {
        return new AccountBindPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_bind_card, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public String getCardNum() {
        return mCardNum.getText().toString().trim();
    }

    @Override
    public String getRealName() {
        return mRealName.getText().toString().trim();
    }

    @Override
    public String getIdNum() {
        return mIdNum.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mPass.getText().toString().trim();
    }

    @OnClick(R.id.btn_Bind)
    public void bind() {
        clearErrorHit();
        if (checkInputValidate()) {
            mPresenter.bindAccount();
        }
    }

    private boolean checkInputValidate() {
        String cardNum = getCardNum();
        if (cardNum.isEmpty() || cardNum.length() != 16) {
            mCardNumWrapper.setError("卡号长度为16位");
            return false;
        }
        String realName = getRealName();
        if (realName.isEmpty() || realName.length() < 3) {
            mRealNameWrapper.setError("真实姓名长度至少3位");
            return false;
        }
        String idNum = getIdNum();
        if (idNum.isEmpty() || idNum.length() != 18) {
            mIdNumWrapper.setError("证件号码长度为18位");
            return false;
        }
        String password = getPassword();
        if (password.isEmpty() || password.length() < 6) {
            mPassWrapper.setError("密码长度不小于6位");
            return false;
        }
        String rePass = mRePass.getText().toString().trim();
        if (!rePass.equals(password)) {
            mRePassWrapper.setError("重复密码不正确");
            return false;
        }
        return true;
    }

    private void clearErrorHit() {
        mCardNumWrapper.setError(null);
        mRealNameWrapper.setError(null);
        mIdNumWrapper.setError(null);
        mPassWrapper.setError(null);
        mRePassWrapper.setError(null);
    }

    @Override
    public void close() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
