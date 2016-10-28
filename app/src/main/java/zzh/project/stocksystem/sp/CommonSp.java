package zzh.project.stocksystem.sp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class CommonSp {
    private SharedPreferences mSharePre;

    protected CommonSp(Context context, String spName) {
        mSharePre = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public void setValue(String key, String value) {
        mSharePre.edit().putString(key, value).commit();
    }

    public String getValue(String key, String defValue) {
        return mSharePre.getString(key, defValue);
    }


    public void setValue(String key, boolean value) {
        mSharePre.edit().putBoolean(key, value).commit();
    }

    public boolean getValue(String key, boolean defValue) {
        return mSharePre.getBoolean(key, defValue);
    }


    public void setValue(String key, int value) {
        mSharePre.edit().putInt(key, value).commit();
    }

    public int getValue(String key, int defValue) {
        return mSharePre.getInt(key, defValue);
    }

    public void setValue(String key, float value) {
        mSharePre.edit().putFloat(key, value).commit();
    }

    public float getValue(String key, float defValue) {
        return mSharePre.getFloat(key, defValue);
    }

    public void setValue(String key, long value) {
        mSharePre.edit().putLong(key, value).commit();
    }

    public long getValue(String key, long defValue) {
        return mSharePre.getLong(key, defValue);
    }

    public void setValue(String key, Set<String> value) {
        mSharePre.edit().putStringSet(key, value).commit();
    }

    public Set<String> getValue(String key, Set<String> defValue) {
        return mSharePre.getStringSet(key, defValue);
    }

    public void registerSpChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharePre.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterSpChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharePre.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
