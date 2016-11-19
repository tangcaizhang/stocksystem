package zzh.project.stocksystem.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import zzh.project.stocksystem.R;
import zzh.project.stocksystem.sp.SettingSp;
import zzh.project.stocksystem.ui.base.BaseStackActivity;
import zzh.project.stocksystem.ui.splash.SplashActivity;

public class LoginActivity extends BaseStackActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentFrame, new LoginFragment());
        transaction.commit();

        initShortcut();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    // 初始化桌面快捷方式
    private void initShortcut() {
        if (SettingSp.getInstance(this).bAddShortcut(true)) {
            SettingSp.getInstance(this).setBAddShortcut(false);

            new AlertDialog.Builder(this).setTitle("提示").setMessage("是否创建快捷方式")
                    .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent addShortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                            String title = getResources().getString(R.string.app_name);
                            Parcelable icon = Intent.ShortcutIconResource.fromContext(LoginActivity.this, R.mipmap.ic_launcher);
                            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
                            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

                            Intent launcherIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

                            sendBroadcast(addShortcutIntent);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();
        }
    }
}
