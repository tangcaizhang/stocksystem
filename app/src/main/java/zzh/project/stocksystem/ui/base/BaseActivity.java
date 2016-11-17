package zzh.project.stocksystem.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import zzh.project.stocksystem.EnvConst;
import zzh.project.stocksystem.util.LoadingBuilder;
import zzh.project.stocksystem.util.ToastUtil;

public abstract class BaseActivity<T extends IPresenter> extends BaseStackActivity implements IView {
    protected T mPresenter;
    protected Dialog mLoading;

    public abstract T createPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EnvConst.DEBUG) {
            Log.d(TAG, "onCreate");
        }
        mPresenter = createPresenter();
    }

    @Override
    public boolean isActive() {
        return !isDestroyed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EnvConst.DEBUG) {
            Log.d(TAG, "onResume");
        }
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        mPresenter.unsubscribe();
        super.onPause();
        if (EnvConst.DEBUG) {
            Log.d(TAG, "onPause");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EnvConst.DEBUG) {
            Log.d(TAG, "onStop");
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
        if (EnvConst.DEBUG) {
            Log.d(TAG, "onDestroy");
        }
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
