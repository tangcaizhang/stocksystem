package zzh.project.stocksystem.ui.splash;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.ui.MainActivity;
import zzh.project.stocksystem.ui.base.BaseActivity;
import zzh.project.stocksystem.ui.login.LoginActivity;

public class SplashActivity extends BaseActivity<SplashContract.Presenter> implements SplashContract.View {
    @BindView(R.id.gif_Splash_Loading)
    GifImageView gifImageView;

    @Override
    public SplashContract.Presenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        ButterKnife.bind(this);
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
    public void onDestroy() {
        Drawable drawable = gifImageView.getDrawable();
        if (drawable != null && drawable instanceof GifDrawable) {
            ((GifDrawable) drawable).recycle();
        }
        super.onDestroy();
    }
}
