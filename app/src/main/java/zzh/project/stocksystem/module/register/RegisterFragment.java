package zzh.project.stocksystem.module.register;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.util.LoadingBuilder;
import zzh.project.stocksystem.util.ToastUtil;

public class RegisterFragment extends Fragment implements RegisterContract.View {
    private static final String TAG = RegisterFragment.class.getSimpleName();
    private Dialog mLoading;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoading = LoadingBuilder.build(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, root);
        return root;
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

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMessage(String msg) {
        ToastUtil.show(msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void showErrorMessage(String errMsg) {
        ToastUtil.show(errMsg, Toast.LENGTH_SHORT);
    }

    @Override
    public void showLoading() {
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        mLoading.cancel();
    }
}
