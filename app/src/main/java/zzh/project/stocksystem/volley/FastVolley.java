package zzh.project.stocksystem.volley;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import zzh.project.stocksystem.MyApplication;

public class FastVolley {

    static final String TAG = "Volley";

    private RequestQueue mRequestQueue;

    private FastVolley(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    private static FastVolley sInstance;

    public static FastVolley getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FastVolley.class) {
                sInstance = new FastVolley(context);
            }
        }
        return sInstance;
    }

    public void start() {
        mRequestQueue.start();
    }

    public void stop() {
        mRequestQueue.stop();
    }

    public void addDefaultRequest(String parentTag, Request<?> request) {
        addRequest(parentTag, request, FastVolley.newDefaultRetryPolicy());
    }

    public void addShortRequest(String parentTag, Request<?> request) {
        addRequest(parentTag, request, FastVolley.newShortRetryPolicy());
    }

    public void addRequest(String parentTag, Request<?> request, RetryPolicy retryPolicy) {
        if (request == null) {
            return;
        }
        if (!MyApplication.getInstance().isNetworkActive()) {
            request.deliverError(new VolleyError(new NetworkError()));
            return;
        }
        if (retryPolicy == null) {
            retryPolicy = FastVolley.newShortRetryPolicy();
        }
        request.setRetryPolicy(retryPolicy);

        request.setTag(getRequestTag(parentTag, request.getTag()));
        mRequestQueue.add(request);
    }

    private static String getRequestTag(String parentTag, Object realTag) {
        if (realTag == null) {
            return parentTag;
        } else {
            return realTag + Integer.toHexString(realTag.hashCode());
        }
    }

    public void cancelAll(String parentTag, Object tag) {
        mRequestQueue.cancelAll(getRequestTag(parentTag, tag));
    }

    public void cancelAll(final String parentTag) {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> arg0) {
                Object tag = arg0.getTag();
                if (tag == null) {
                    return false;
                }
                if (tag instanceof String) {
                    return ((String) tag).indexOf(parentTag) != -1;
                }
                return false;
            }
        });
    }

    /* 请求一次，重试两次，最长请求时间可达30s，5s+10s+15s，普通接口使用此默认配置 */
    public static DefaultRetryPolicy newDefaultRetryPolicy() {
        return new DefaultRetryPolicy(5000, 2, 1);
    }

    /* 请求一次，重试0次，最长请求时间可达5s，一些很小的数据请求用此接口 */
    public static DefaultRetryPolicy newShortRetryPolicy() {
        return new DefaultRetryPolicy(5000, 0, 0);
    }
}
