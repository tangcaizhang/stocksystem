package zzh.project.stocksystem.module.splash;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.module.login.LoginActivity;
import zzh.project.stocksystem.module.main.MainActivity;
import zzh.project.stocksystem.util.ToastUtil;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private SplashContract.Presenter mPresenter;
    private boolean isActive = false;

    @BindView(R.id.gif_Splash_Loading)
    GifImageView gifImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mPresenter = new SplashPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean isActive() {
        return isActive;
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
    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        Drawable drawable = gifImageView.getDrawable();
        if (drawable != null && drawable instanceof GifDrawable) {
            ((GifDrawable) drawable).recycle();
        }
        super.onDestroy();
    }
}
