package zzh.project.stocksystem.helper;


import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;

import zzh.project.stocksystem.model.exception.StockSystemException;

public class MsgHelper {
    // 从异常类转换成错误提示
    public static String getErrorMsg(Throwable error) {
        if (error instanceof StockSystemException) {
            return error.getMessage();
        } else if (error instanceof TimeoutError || error.getCause() instanceof TimeoutError) {
            return "连接超时，请检测网络";
        } else if (error instanceof NetworkError || error.getCause() instanceof NetworkError) {
            return "请检测网络链接";
        } else {
            return "未知异常, " + error.getMessage();
        }
    }
}
