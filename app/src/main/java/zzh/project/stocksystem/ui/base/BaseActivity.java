package zzh.project.stocksystem.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import zzh.project.stocksystem.util.LoadingBuilder;
import zzh.project.stocksystem.util.ToastUtil;

public abstract class BaseActivity<T extends IPresenter> extends BaseStackActivity implements IView {
    protected T mPresenter;
    protected Dialog mLoading;

    public abstract T createPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Override
    public boolean isActive() {
        return !isDestroyed();
    }

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
            mLoading = LoadingBuilder.build(this);
        }
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        if (mLoading == null) {
            mLoading = LoadingBuilder.build(this);
        }
        mLoading.cancel();
    }
}
