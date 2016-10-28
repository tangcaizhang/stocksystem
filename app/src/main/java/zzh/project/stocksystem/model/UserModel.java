package zzh.project.stocksystem.model;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import zzh.project.stocksystem.ApiUrlConst;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.ServerErrorCode;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.sp.UserSp;
import zzh.project.stocksystem.volley.FastVolley;

public class UserModel implements IUserModel {
    public static final String TAG = UserModel.class.getSimpleName();
    private static UserModel sInstance;
    private UserSp mUserSp;
    private FastVolley mFastVolley;
    private String HASHCODE;

    private UserModel(Context context) {
        HASHCODE = Integer.toHexString(this.hashCode()) + "@";
        mUserSp = UserSp.getInstance(context);
        mFastVolley = FastVolley.getInstance(context);
    }

    public static UserModel getInstance() {
        if (sInstance == null) {
            sInstance = new UserModel(MyApplication.getInstance());
        }
        return sInstance;
    }

    @Override
    public String getHistoryUser() {
        return mUserSp.getUsername(null);
    }

    @Override
    public void login(String username, String password, final LoginCallback callback) {
        mFastVolley.cancelAll(HASHCODE, "login");
        try {
            JSONObject reqParams = new JSONObject().put("username", username).put("password", password);
            JsonObjectRequest request = new JsonObjectRequest(ApiUrlConst.SERVER_LOGIN, reqParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "response " + response);
                    int errcode = response.optInt("errcode");
                    if (errcode == ServerErrorCode.RESPONSE_SUCCESS) {
                        callback.onLoginSuccess();
                    } else if (errcode == ServerErrorCode.RESPONSE_FAILED) {
                        callback.onLoginError("用户名或密码不正确");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onLoginError(MsgHelper.getErrorMsg(error));
                }
            });
            request.setTag("login");
            mFastVolley.addShortRequest(HASHCODE, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setHistoryUser(String username) {
        mUserSp.setUsername(username);
    }

    @Override
    public boolean checkAccessToken() {
        String accessToken = mUserSp.getAccessToken(null);
        if (accessToken == null) {
            return false;
        }
        long expiresIn = mUserSp.getExpiresIn(0);
        if (expiresIn < SystemClock.currentThreadTimeMillis()) {
            return false;
        }
        return true;
    }

    @Override
    public String getAccessToken() {
        return mUserSp.getAccessToken(null);
    }

    public void destroy() {
        mFastVolley.cancelAll(HASHCODE);
    }
}
