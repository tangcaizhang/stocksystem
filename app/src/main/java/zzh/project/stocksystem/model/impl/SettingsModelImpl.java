package zzh.project.stocksystem.model.impl;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.model.SettingsModel;
import zzh.project.stocksystem.sp.SettingSp;

public class SettingsModelImpl implements SettingsModel {
    private final String TAG = this.getClass().getSimpleName();
    private static SettingsModelImpl sInstance;
    private SettingSp mSettingSp;

    private SettingsModelImpl(Context context) {
        mSettingSp = SettingSp.getInstance(context);
    }

    public static SettingsModelImpl getInstance() {
        if (sInstance == null) {
            sInstance = new SettingsModelImpl(MyApplication.getInstance());
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
