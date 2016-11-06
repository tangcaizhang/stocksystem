package zzh.project.stocksystem.model.impl;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zzh.project.stocksystem.ApiUrl;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.ServerErrorCode;
import zzh.project.stocksystem.bean.AccessToken;
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.bean.HoldStockBean;
import zzh.project.stocksystem.bean.TradeBean;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.helper.JPushHelper;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.sp.UserSp;
import zzh.project.stocksystem.volley.FastVolley;
import zzh.project.stocksystem.volley.GsonObjectRequest;

public class UserModelImpl implements UserModel {
    public static final String TAG = UserModelImpl.class.getSimpleName();
    private static UserModelImpl sInstance;
    private UserSp mUserSp;
    private FastVolley mFastVolley;
    private String HASHCODE;
    private AccessToken mAccessToken;

    private UserModelImpl(Context context) {
        HASHCODE = Integer.toHexString(this.hashCode()) + "@";
        mUserSp = UserSp.getInstance(context);
        mFastVolley = FastVolley.getInstance(context);
        mAccessToken = getAccessToken();
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
    public void login(final String username, String password, final Callback2<AccessToken, String> callback) {
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
                            JPushHelper.setAlias(username);
                        }
                    } else {
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
                } else {
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
    public void getInfo(final Callback2<UserBean, String> callback) {
        String url = ApiUrl.SERVER_GET_INFO + "?access_token=" + mAccessToken.accessToken;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                int errcode = response.get("errcode").getAsInt();
                if (errcode == ServerErrorCode.SUCCESS) {
                    UserBean bean = new Gson().fromJson(response.get("data"), UserBean.class);
                    callback.onSuccess(bean);
                } else if (errcode == ServerErrorCode.ACCESS_TOKEN_EXPIRES) {
                    callback.onError(response.get("errmsg").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("getInfo");
        mFastVolley.addShortRequest(HASHCODE, request);
    }

    @Override
    public void setHistoryUser(String username) {
        mUserSp.setUsername(username);
    }

    @Override
    public void checkAccessToken(final Callback2<Void, Void> callback) {
        AccessToken accessToken = getAccessToken();
        Log.d(TAG, "access_token -> " + accessToken);
        if (accessToken.accessToken == null || accessToken.accessToken.isEmpty()) {
            callback.onError(null);
        }
        if (accessToken.expiresIn < SystemClock.currentThreadTimeMillis()) {
            callback.onError(null);
        }
        mFastVolley.cancelAll(HASHCODE, "check");
        String url = ApiUrl.SERVER_CHECK + "?access_token=" + mAccessToken.accessToken;
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "checkAccessToken resp " + response);
                int errcode = response.optInt("errcode");
                if (errcode == ServerErrorCode.SUCCESS) {
                    callback.onSuccess(null);
                    JPushHelper.setAlias(mUserSp.getUsername(""));
                } else if (errcode == ServerErrorCode.ACCESS_TOKEN_EXPIRES) {
                    callback.onError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(null);
            }
        });
        request.setTag("check");
        mFastVolley.addShortRequest(HASHCODE, request);
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
        mAccessToken = accessToken;
    }

    @Override
    public void favor(String gid, final Callback2<Void, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "favor");
        try {
            JSONObject reqParams = new JSONObject().put("gid", gid);
            String url = ApiUrl.SERVER_FAVOR + "?access_token=" + mAccessToken.accessToken;
            JsonObjectRequest request = new JsonObjectRequest(url, reqParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "favor resp " + response);
                    int errcode = response.optInt("errcode");
                    if (errcode == ServerErrorCode.SUCCESS) {
                        callback.onSuccess(null);
                    } else if (errcode == ServerErrorCode.ACCESS_TOKEN_EXPIRES) {
                        callback.onError(response.optString("errmsg"));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(MsgHelper.getErrorMsg(error));
                }
            });
            request.setTag("favor");
            mFastVolley.addShortRequest(HASHCODE, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unFavor(String gid, final Callback2<Void, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "unfavor");
        try {
            JSONObject reqParams = new JSONObject().put("gid", gid);
            String url = ApiUrl.SERVER_UNFAVOR + "?access_token=" + mAccessToken.accessToken;
            JsonObjectRequest request = new JsonObjectRequest(url, reqParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "favor resp " + response);
                    int errcode = response.optInt("errcode");
                    if (errcode == ServerErrorCode.SUCCESS) {
                        callback.onSuccess(null);
                    } else if (errcode == ServerErrorCode.ACCESS_TOKEN_EXPIRES) {
                        callback.onError(response.optString("errmsg"));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(MsgHelper.getErrorMsg(error));
                }
            });
            request.setTag("unfavor");
            mFastVolley.addShortRequest(HASHCODE, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listFavor(final Callback2<List<String>, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "listfavor");
        String url = ApiUrl.SERVER_LIST_FAVOR + "?access_token=" + mAccessToken.accessToken;
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "listFavor resp " + response);
                int errcode = response.optInt("errcode");
                if (errcode == ServerErrorCode.SUCCESS) {
                    JSONArray jDataArr = response.optJSONArray("data");
                    if (jDataArr != null) {
                        List<String> result = new ArrayList<>(jDataArr.length());
                        for (int i = 0; i < jDataArr.length(); i++) {
                            result.add(jDataArr.optJSONObject(i).optString("gid"));
                        }
                        callback.onSuccess(result);
                    }
                } else if (errcode == ServerErrorCode.ACCESS_TOKEN_EXPIRES) {
                    callback.onError(response.optString("errmsg"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("listfavor");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    @Override
    public void getAccount(final Callback2<AccountBean, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "getAccount");
        String url = ApiUrl.SERVER_GET_ACCOUNT + "?access_token=" + mAccessToken.accessToken;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Log.d(TAG, "getAccount resp " + response);
                int errcode = response.get("errcode").getAsInt();
                if (errcode == ServerErrorCode.SUCCESS) {
                    AccountBean bean = new Gson().fromJson(response.get("data"), AccountBean.class);
                    callback.onSuccess(bean);
                } else {
                    callback.onError(response.get("errmsg").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("getAccount");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    @Override
    public void bindAccount(AccountBean accountBean, final Callback2<Void, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "bindAccount");
        String url = ApiUrl.SERVER_BIND_ACCOUNT + "?access_token=" + mAccessToken.accessToken;
        String reqBody = new Gson().toJson(accountBean);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int errcode = response.optInt("errcode");
                if (errcode == ServerErrorCode.SUCCESS) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(response.optString("errmsg"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("bindAccount");
        mFastVolley.addShortRequest(HASHCODE, request);
    }

    @Override
    public void recharge(String cardNum, String password, float money, final Callback2<Void, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "recharge");
        String url = ApiUrl.SERVER_RECHARGE + "?access_token=" + mAccessToken.accessToken;
        JSONObject reqParams = new JSONObject();
        try {
            reqParams.put("cardNum", cardNum);
            reqParams.put("password", password);
            reqParams.put("money", money);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int errcode = response.optInt("errcode");
                if (errcode == ServerErrorCode.SUCCESS) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(response.optString("errmsg"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("recharge");
        mFastVolley.addShortRequest(HASHCODE, request);
    }

    @Override
    public void listTrade(final Callback2<List<TradeBean>, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "listTrade");
        String url = ApiUrl.SERVER_LIST_TRADE + "?access_token=" + mAccessToken.accessToken;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Log.d(TAG, "listTrade resp " + response);
                int errcode = response.get("errcode").getAsInt();
                if (errcode == ServerErrorCode.SUCCESS) {
                    JsonArray jArray = response.get("data").getAsJsonArray();
                    List<TradeBean> beans = new ArrayList<>(jArray.size());
                    for (int i = 0; i < jArray.size(); i++) {
                        TradeBean tradeBean = new Gson().fromJson(jArray.get(i), TradeBean.class);
                        beans.add(tradeBean);
                    }
                    callback.onSuccess(beans);
                } else {
                    callback.onError(response.get("errmsg").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("listTrade");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    @Override
    public void buy(String gid, String name, float uPrice, int amount, final Callback2<Void, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "buy");
        String url = ApiUrl.SERVER_BUY + "?access_token=" + mAccessToken.accessToken;
        JSONObject reqParams = new JSONObject();
        try {
            reqParams.put("gid", gid);
            reqParams.put("name", name);
            reqParams.put("uPrice", uPrice);
            reqParams.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "buy resp " + response);
                int errcode = response.optInt("errcode");
                if (errcode == ServerErrorCode.SUCCESS) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(response.optString("errmsg"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("buy");
        mFastVolley.addShortRequest(HASHCODE, request);
    }

    @Override
    public void sell(String gid, String name, float uPrice, int amount, final Callback2<Void, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "sell");
        String url = ApiUrl.SERVER_SELL + "?access_token=" + mAccessToken.accessToken;
        JSONObject reqParams = new JSONObject();
        try {
            reqParams.put("gid", gid);
            reqParams.put("name", name);
            reqParams.put("uPrice", uPrice);
            reqParams.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "sell resp " + response);
                int errcode = response.optInt("errcode");
                if (errcode == ServerErrorCode.SUCCESS) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(response.optString("errmsg"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("sell");
        mFastVolley.addShortRequest(HASHCODE, request);
    }

    @Override
    public void listHoldStock(final Callback2<List<HoldStockBean>, String> callback) {
        mFastVolley.cancelAll(HASHCODE, "listHoldStock");
        String url = ApiUrl.SERVER_LIST_STOCK + "?access_token=" + mAccessToken.accessToken;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Log.d(TAG, "listHoldStock resp " + response);
                int errcode = response.get("errcode").getAsInt();
                if (errcode == ServerErrorCode.SUCCESS) {
                    JsonArray jArray = response.get("data").getAsJsonArray();
                    List<HoldStockBean> beans = new ArrayList<>(jArray.size());
                    for (int i = 0; i < jArray.size(); i++) {
                        HoldStockBean stockBean = new Gson().fromJson(jArray.get(i), HoldStockBean.class);
                        beans.add(stockBean);
                    }
                    callback.onSuccess(beans);
                } else {
                    callback.onError(response.get("errmsg").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("listHoldStock");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    public void destroy() {
        mFastVolley.cancelAll(HASHCODE);
    }

}
