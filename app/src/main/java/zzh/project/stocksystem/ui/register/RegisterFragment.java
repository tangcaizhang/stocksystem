package zzh.project.stocksystem.ui.register;

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

public class RegisterFragment extends BaseFragment implements RegisterContract.View {
    private static final String TAG = RegisterFragment.class.getSimpleName();
    private RegisterContract.Presenter mPresenter;

    @BindView(R.id.til_Register_Username_Wrapper)
    TextInputLayout mUsernameWrapper;
    @BindView(R.id.et_Register_Username)
    EditText mUsername;
    @BindView(R.id.til_Register_Nick_Wrapper)
    TextInputLayout mNickWrapper;
    @BindView(R.id.et_Register_Nick)
    EditText mNick;
    @BindView(R.id.til_Register_Email_Wrapper)
    TextInputLayout mEmailWrapper;
    @BindView(R.id.et_Register_Email)
    EditText mEmail;
    @BindView(R.id.til_Register_Pass_Wrapper)
    TextInputLayout mPassWrapper;
    @BindView(R.id.et_Register_Pass)
    EditText mPass;
    @BindView(R.id.til_Register_RePass_Wrapper)
    TextInputLayout mRePassWrapper;
    @BindView(R.id.et_Register_RePass)
    EditText mRePass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new RegisterPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_register, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onDetach() {
        mPresenter.destroy();
        super.onDetach();
    }

    @OnClick(R.id.btn_Register)
    public void register(View view) {
        clearErrorHit();
        if (checkInputValidate()) {
            mPresenter.register();
        }
    }

    private void clearErrorHit() {
        mUsernameWrapper.setError(null);
        mNickWrapper.setError(null);
        mEmailWrapper.setError(null);
        mPassWrapper.setError(null);
        mRePassWrapper.setError(null);
    }

    private boolean checkInputValidate() {
        String username = mUsername.getText().toString().trim();
        if (username.isEmpty()) {
            mUsernameWrapper.setError("请输入用户名");
            return false;
        }
        if (!username.matches("^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}")) {
            mUsernameWrapper.setError("手机号格式不正确");
            return false;
        }
        String nick = mNick.getText().toString().trim();
        if (nick.isEmpty() || nick.length() < 3) {
            mNickWrapper.setError("昵称长度大于3");
            return false;
        }
        String email = mEmail.getText().toString().trim();
        if (email.isEmpty() || !email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            mEmailWrapper.setError("邮箱格式不正确");
            return false;
        }
        String password = mPass.getText().toString().trim();
        if (password.isEmpty() || password.length() < 6) {
            mPassWrapper.setError("密码长度大于6");
            return false;
        }
        String rePass = mRePass.getText().toString().trim();
        if (!rePass.equals(password)) {
            mRePassWrapper.setError("重复密码不正确");
            return false;
        }
        return true;
    }

    @Override
    public String getUsername() {
        return mUsername.getText().toString();
    }

    @Override
    public String getEmail() {
        return mEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPass.getText().toString();
    }

    @Override
    public String getNick() {
        return mNick.getText().toString();
    }

    @Override
    public void close() {
        getActivity().finish();
    }

}
