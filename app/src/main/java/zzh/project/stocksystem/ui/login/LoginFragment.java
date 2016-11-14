package zzh.project.stocksystem.ui.login;

import android.content.Intent;
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
import zzh.project.stocksystem.ui.MainActivity;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.register.RegisterActivity;

public class LoginFragment extends BaseFragment<LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.et_Login_Username)
    EditText mUsername;
    @BindView(R.id.et_Login_Pass)
    EditText mPassword;
    @BindView(R.id.til_Login_UsernameWrapper)
    TextInputLayout mUsernameWrapper;
    @BindView(R.id.til_Login_Pass_Wrapper)
    TextInputLayout mPasswordWrapper;

    @Override
    public LoginContract.Presenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_login, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.btn_Login)
    public void login(View view) {
        clearErrorHit();
        if (checkInputValidate()) {
            mPresenter.login();
        }
    }

    private void clearErrorHit() {
        mUsernameWrapper.setError(null);
        mPasswordWrapper.setError(null);
    }

    // 输入校验view来做，不经过model的不由presenter处理
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
        String password = mPassword.getText().toString().trim();
        if (password.isEmpty()) {
            mPasswordWrapper.setError("请输入密码");
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_Login_Register)
    public void toRegisterView(View view) {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public String getUsername() {
        return mUsername.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString().trim();
    }

    @Override
    public void clearUsername() {
        mUsername.setText("");
    }

    @Override
    public void clearPassword() {
        mPassword.setText("");
    }

    @Override
    public void setUsername(String username) {
        mUsername.setText(username);
    }

    @Override
    public void toMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
