package zzh.project.stocksystem.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.login.LoginActivity;

public class SettingsFragment extends BaseFragment implements SettingsContract.View {

    private SettingsContract.Presenter mPresenter;
    @BindView(R.id.sw_Settings_Push)
    SwitchCompat mPushSwitch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SettingsPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_settings, container, false);
        ButterKnife.bind(this, root);
        mPushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPresenter.enablePush();
                } else {
                    mPresenter.disablePush();
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDetach() {
        mPresenter.destroy();
        super.onDetach();
    }

    @Override
    public void switchPush(boolean checked) {
        if (mPushSwitch.isChecked() != checked) {
            mPushSwitch.setChecked(checked);
        }
    }

    @OnClick(R.id.rl_Settings_Logout)
    public void logout() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("退出")
                .setMessage("确定要退出应用吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.logout();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setCancelable(true).create();
        dialog.show();
    }

    @Override
    public void toLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
