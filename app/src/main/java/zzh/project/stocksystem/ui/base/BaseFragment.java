package zzh.project.stocksystem.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import zzh.project.stocksystem.util.LoadingBuilder;
import zzh.project.stocksystem.util.ToastUtil;

public abstract class BaseFragment<T extends IPresenter> extends Fragment implements IView {
    protected String TAG = this.getClass().getSimpleName();
    protected T mPresenter;
    protected Dialog mLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    public abstract T createPresenter();

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        mPresenter.unsubscribe();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
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
        if (mLoading == null) {
            mLoading = LoadingBuilder.build(getContext());
        }
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        if (mLoading == null) {
            mLoading = LoadingBuilder.build(getContext());
        }
        mLoading.cancel();
    }
}
