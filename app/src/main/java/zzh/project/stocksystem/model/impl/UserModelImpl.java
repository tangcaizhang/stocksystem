package zzh.project.stocksystem.model.impl;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import zzh.project.stocksystem.ApiUrl;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.ServerErrorCode;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.bean.AccessToken;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.sp.UserSp;
import zzh.project.stocksystem.volley.FastVolley;

public class UserModelImpl implements UserModel {
    public static final String TAG = UserModelImpl.class.getSimpleName();
    private static UserModelImpl sInstance;
    private UserSp mUserSp;
    private FastVolley mFastVolley;
    private String HASHCODE;

    private UserModelImpl(Context context) {
        HASHCODE = Integer.toHexString(this.hashCode()) + "@";
        mUserSp = UserSp.getInstance(context);
        mFastVolley = FastVolley.getInstance(context);
    }

    public static UserModelImpl getInstance() {
        if (sInstance == null) {
            sInstance = new UserModelImpl(MyApplication.getInstance());
        }
        return sInstance;
    }

    @Override
    public String getHistoryUser() {
        return mUserSp.getUsername(null);
    }

    @Override
    public void login(String username, String password, final Callback2<AccessToken, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "login");
        try {
            JSONObject reqParams = new JSONObject().put("username", username).put("password", password);
            JsonObjectRequest request = new JsonObjectRequest(ApiUrl.SERVER_LOGIN, reqParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "login resp " + response);
                    int errcode = response.optInt("errcode");
                    if (errcode == ServerErrorCode.SUCCESS) {
                        JSONObject jData = response.optJSONObject("data");
                        if (jData != null) {
                            AccessToken accessToken = new AccessToken();
                            accessToken.accessToken = jData.optString("access_token");
                            accessToken.expiresIn = jData.optLong("expires");
                            callback.onSuccess(accessToken);
                        }
                    } else if (errcode == ServerErrorCode.PASS_WRONG) {
                        callback.onError("用户名或密码不正确");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(MsgHelper.getErrorMsg(error));
                }
            });
            request.setTag("login");
            mFastVolley.addShortRequest(HASHCODE, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        mUserSp.clearUserInfo();
    }

    @Override
    public void register(UserBean userBean, final Callback2<Void, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "register");
        String reqBody = new Gson().toJson(userBean);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl.SERVER_REGISTER, reqBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int errcode = response.optInt("errcode");
                if (errcode == ServerErrorCode.SUCCESS) {
                    callback.onSuccess(null);
                } else if (errcode == ServerErrorCode.USERNAME_ALREADY_EXISTS) {
                    callback.onError("该用户已存在");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("register");
        mFastVolley.addShortRequest(HASHCODE, request);
    }

    @Override
    public void setHistoryUser(String username) {
        mUserSp.setUsername(username);
    }

    @Override
    public boolean checkAccessToken() {
        AccessToken accessToken = getAccessToken();
        Log.d(TAG, "access_token -> " + accessToken);
        if (accessToken.accessToken == null || accessToken.accessToken.isEmpty()) {
            return false;
        }
        if (accessToken.expiresIn < SystemClock.currentThreadTimeMillis()) {
            return false;
        }
        return true;
    }

    private AccessToken getAccessToken() {
        AccessToken accessToken = new AccessToken();
        accessToken.accessToken = mUserSp.getAccessToken(null);
        accessToken.expiresIn = mUserSp.getExpiresIn(0);
        return accessToken;
    }

    @Override
    public void saveAccessToken(AccessToken accessToken) {
        mUserSp.setAccessToken(accessToken.accessToken);
        mUserSp.setExpiresIn(accessToken.expiresIn);
    }

    public void destroy() {
        mFastVolley.cancelAll(HASHCODE);
    }
}
