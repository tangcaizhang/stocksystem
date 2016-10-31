package zzh.project.stocksystem.ui.base;

import android.os.Bundle;

import com.zhy.autolayout.AutoLayoutActivity;

import zzh.project.stocksystem.MyApplication;

/**
 * 加入了堆栈的Activity
 */
public class BaseActivity extends AutoLayoutActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().push(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstance().pop(this);
        if (isFinishing()) {
            if (!ActivityStack.getInstance().has()) {
                MyApplication.getInstance().destroy();
            }
        }
    }
}
