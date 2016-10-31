package zzh.project.stocksystem.helper;


import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class MsgHelper {
    public static String getErrorMsg(VolleyError error) {
        if (error instanceof TimeoutError || error.getCause() instanceof TimeoutError) {
            return "连接超时，请检测网络";
        } else if (error instanceof NetworkError || error.getCause() instanceof NetworkError) {
            return "请检测网络链接";
        } else {
            return "未知异常, " + error.getMessage();
        }
    }
}
