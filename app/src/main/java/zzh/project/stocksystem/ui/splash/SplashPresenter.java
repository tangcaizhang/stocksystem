package zzh.project.stocksystem.ui.splash;


import android.os.Handler;
import android.os.SystemClock;

import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.impl.UserModelImpl;

public class SplashPresenter implements SplashContract.Presenter {
    private static final String TAG = SplashPresenter.class.getSimpleName();
    private SplashContract.View mView;
    private UserModelImpl mUserModel;
    private Handler mHandler = new Handler();
    private Runnable mGoTask;

    public SplashPresenter(SplashContract.View view) {
        mView = view;
        mUserModel = UserModelImpl.getInstance();
    }

    @Override
    public void start() {
        doInit();
    }

    @Override
    public void destroy() {
        if (mGoTask != null) {
            mHandler.removeCallbacks(mGoTask);
            mGoTask = null;
        }
        mView = null;
    }

    private void doInit() {
        final long start = SystemClock.currentThreadTimeMillis();
        mUserModel.checkAccessToken(new Callback2<Void, Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mGoTask = new Runnable() {
                    @Override
                    public void run() {
                        mView.toMainActivity();
                    }
                };
                mHandler.postDelayed(mGoTask, 1000 - (SystemClock.currentThreadTimeMillis() - start));
            }

            @Override
            public void onError(Void aVoid) {
                mGoTask = new Runnable() {
                    @Override
                    public void run() {
                        mView.toLoginActivity();
                    }
                };
                mHandler.postDelayed(mGoTask, 1000 - (SystemClock.currentThreadTimeMillis() - start));
            }
        });
    }
}
