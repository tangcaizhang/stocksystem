package zzh.project.stocksystem.module.splash;


import android.os.Handler;
import android.os.SystemClock;

import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.util.ThreadManager;

public class SplashPresenter implements SplashContract.Presenter {
    private static final String TAG = SplashPresenter.class.getSimpleName();
    private SplashContract.View mView;
    private UserModel mUserModel;
    private Handler mHandler = new Handler();
    private Runnable mGoTask;

    public SplashPresenter(SplashContract.View view) {
        mView = view;
        mUserModel = UserModel.getInstance();
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
        ThreadManager.getPool().execute(new Runnable() {
            @Override
            public void run() {
                final boolean validResult = mUserModel.checkAccessToken();
                mGoTask = new Runnable() {
                    @Override
                    public void run() {
                        if (validResult) {
                            mView.toMainActivity();
                        } else {
                            mView.toLoginActivity();
                        }
                    }
                };
                mHandler.postDelayed(mGoTask, 1000 - (SystemClock.currentThreadTimeMillis() - start));
            }
        });
    }
}
