package zzh.project.stocksystem.model.impl;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.model.SettingsModel;
import zzh.project.stocksystem.sp.SettingSp;

public class SettingsModelImpl implements SettingsModel {
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
        if (JPushInterface.isPushStopped(MyApplication.getInstance())) {
            JPushInterface.resumePush(MyApplication.getInstance());
        }
        mSettingSp.setEnablePush(true);
    }

    @Override
    public void disablePush() {
        if (!JPushInterface.isPushStopped(MyApplication.getInstance())) {
            JPushInterface.stopPush(MyApplication.getInstance());
        }
        mSettingSp.setEnablePush(true);
    }

    @Override
    public boolean isEnablePush() {
        return mSettingSp.isEnablePush(true);
    }
}
