package zzh.project.stocksystem.sp;

import android.content.Context;


public class UserSp extends CommonSp {
    private static final String SP_NAME = "login_user_info";// FILE_NAME
    private static UserSp instance;

    /* known key */
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";// 过期时间，时间戳

    public static UserSp getInstance(Context context) {
        if (instance == null) {
            synchronized (UserSp.class) {
                if (instance == null) {
                    instance = new UserSp(context);
                }
            }
        }
        return instance;
    }

    private UserSp(Context context) {
        super(context, SP_NAME);
    }

    public String getUsername(String defaultValue) {
        return getValue(KEY_USERNAME, defaultValue);
    }

    public void setUsername(String value) {
        setValue(KEY_USERNAME, value);
    }

    public String getAccessToken(String defaultValue) {
        return getValue(KEY_ACCESS_TOKEN, defaultValue);
    }

    public void setAccessToken(String value) {
        setValue(KEY_ACCESS_TOKEN, value);
    }

    public long getExpiresIn(long defaultValue) {
        return getValue(KEY_EXPIRES_IN, defaultValue);
    }

    public void setExpiresIn(long value) {
        setValue(KEY_EXPIRES_IN, value);
    }

    // 注销登录时，将其他数据清空，只保留Username
    public void clearUserInfo() {
        setAccessToken("");
        setExpiresIn(0);
    }
}
