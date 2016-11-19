package zzh.project.stocksystem.sp;

import android.content.Context;

public class SettingSp extends CommonSp {
    private static final String SP_NAME = "settings";// FILE_NAME
    private static SettingSp instance;

    /* known key */
    private static final String KEY_PUSH = "jpush";
    private static final String KEY_ADD_SHORTCUT = "shortcut";

    public static SettingSp getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingSp.class) {
                if (instance == null) {
                    instance = new SettingSp(context);
                }
            }
        }
        return instance;
    }

    private SettingSp(Context context) {
        super(context, SP_NAME);
    }

    public boolean isEnablePush(boolean defaultValue) {
        return getValue(KEY_PUSH, defaultValue);
    }

    public void setEnablePush(boolean value) {
        setValue(KEY_PUSH, value);
    }

    public boolean bAddShortcut(boolean defaultValue) {
        return getValue(KEY_ADD_SHORTCUT, defaultValue);
    }

    public void setBAddShortcut(boolean value) {
        setValue(KEY_ADD_SHORTCUT, value);
    }
}
