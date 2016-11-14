package zzh.project.stocksystem.ui.base;

import android.os.Build;
import android.os.Bundle;

import com.zhy.autolayout.AutoLayoutActivity;

import zzh.project.stocksystem.MyApplication;

/**
 * 加入了堆栈的Activity
 */
public class BaseStackActivity extends AutoLayoutActivity {
    protected String TAG = this.getClass().getSimpleName();
    private boolean isDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().push(this);
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        ActivityStack.getInstance().pop(this);
        if (isFinishing()) {
            if (!ActivityStack.getInstance().has()) {
                MyApplication.getInstance().destroy();
            }
        }
        super.onDestroy();
    }

    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            return super.isDestroyed();
        }
        return isDestroyed;
    }
}
