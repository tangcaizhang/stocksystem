package zzh.project.stocksystem.model.impl;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.model.SettingsManager;
import zzh.project.stocksystem.sp.SettingSp;

public class SettingsManagerImpl implements SettingsManager {
    private final String TAG = this.getClass().getSimpleName();
    private static SettingsManagerImpl sInstance;
    private SettingSp mSettingSp;

    private SettingsManagerImpl(Context context) {
        mSettingSp = SettingSp.getInstance(context);
    }

    public static SettingsManagerImpl getInstance() {
        if (sInstance == null) {
            sInstance = new SettingsManagerImpl(MyApplication.getInstance());
        }
        return sInstance;
    }

    @Override
    public void enablePush() {
        // 恢复push服务
        if (JPushInterface.isPushStopped(MyApplication.getInstance())) {
            JPushInterface.resumePush(MyApplication.getInstance());
        }
        // 保存配置项
        mSettingSp.setEnablePush(true);
    }

    @Override
    public void disablePush() {
        // 暂停push服务
        if (!JPushInterface.isPushStopped(MyApplication.getInstance())) {
            JPushInterface.stopPush(MyApplication.getInstance());
        }
        // 保存配置项
        mSettingSp.setEnablePush(true);
    }

    @Override
    public boolean isEnablePush() {
        return mSettingSp.isEnablePush(true);
    }
}
