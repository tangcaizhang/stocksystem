package zzh.project.stocksystem.helper;

import android.util.Log;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import zzh.project.stocksystem.MyApplication;

public class JPushHelper {

    // 向jpush服务器设置一个别名
    public static void setAlias(String alias) {
        JPushInterface.setAlias(MyApplication.getInstance(), alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d("JPushHelper", "gotResult " + i);
            }
        });
    }
}
