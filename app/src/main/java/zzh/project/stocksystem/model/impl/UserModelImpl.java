package zzh.project.stocksystem.model.impl;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import zzh.project.stocksystem.ApiUrl;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.ServerErrorCode;
import zzh.project.stocksystem.bean.AccessToken;
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.bean.HoldStockBean;
import zzh.project.stocksystem.bean.TradeBean;
import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.model.UserModel;
import zzh.project.stocksystem.model.exception.StockSystemException;
import zzh.project.stocksystem.sp.UserSp;
import zzh.project.stocksystem.volley.FastVolley;
import zzh.project.stocksystem.volley.GsonObjectRequest;

public class UserModelImpl implements UserModel {
    private final String TAG = this.getClass().getSimpleName();
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
    public Observable<AccessToken> login(final String username, final String password) {
        final String url = ApiUrl.SERVER_LOGIN;
        mFastVolley.cancelAll(url);
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                try {
                    JSONObject reqParams = new JSONObject().put("username", username).put("password", password);
                    RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
                    JsonObjectRequest request = new JsonObjectRequest(url, reqParams, requestFuture, requestFuture);
                    request.setTag(url);
                    mFastVolley.addShortRequest(HASHCODE, request);
                    try {
                        JSONObject obj = requestFuture.get();
                        int errcode = obj.optInt("errcode", -1);
                        if (errcode == ServerErrorCode.SUCCESS && obj.has("data")) {
                            subscriber.onNext(obj.getJSONObject("data"));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new StockSystemException("用户名或密码不正确"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "login get resp failed, " + e.getClass() + ":" + e.getMessage());
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "login gen param failed, " + e.getClass() + ":" + e.getMessage());
                }
            }
        }).map(new Func1<JSONObject, AccessToken>() {
            @Override
            public AccessToken call(JSONObject jsonObject) {
                AccessToken accessToken = new AccessToken();
                accessToken.accessToken = jsonObject.optString("access_token");
                accessToken.expiresIn = jsonObject.optLong("expires");
                return accessToken;
            }
        });
    }

    @Override
    public void logout() {
        mUserSp.clearUserInfo();
    }

    @Override
    public void register(UserBean userBean) throws Exception {
        String url = ApiUrl.SERVER_REGISTER;
        mFastVolley.cancelAll(url);
        String reqBody = new Gson().toJson(userBean);
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqBody, requestFuture, requestFuture);
        request.setTag(url);
        mFastVolley.addShortRequest(HASHCODE, request);
        JSONObject obj = requestFuture.get();
        int errcode = obj.optInt("errcode", -1);
        if (errcode != ServerErrorCode.SUCCESS) {
            throw new StockSystemException("该用户已存在");
        }
    }

    @Override
    public Observable<UserBean> getInfo() {
        return Observable.create(new Observable.OnSubscribe<JsonElement>() {
            @Override
            public void call(Subscriber<? super JsonElement> subscriber) {
                String url = ApiUrl.SERVER_GET_INFO + "?access_token=" + mAccessToken.accessToken;
                mFastVolley.cancelAll(url);
                RequestFuture<JsonObject> requestFuture = RequestFuture.newFuture();
                GsonObjectRequest request = new GsonObjectRequest(url, requestFuture, requestFuture);
                request.setTag(url);
                mFastVolley.addShortRequest(HASHCODE, request);
                try {
                    JsonObject obj = requestFuture.get();
                    int errcode = obj.get("errcode").getAsInt();
                    if (errcode == ServerErrorCode.SUCCESS && obj.has("data")) {
                        subscriber.onNext(obj.get("data"));
                        subscriber.onCompleted();
                    } else if (errcode == ServerErrorCode.ACCESS_TOKEN_EXPIRES) {
                        subscriber.onError(new StockSystemException(obj.get("errmsg").getAsString()));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getInfo " + e.getClass() + ":" + e.getMessage());
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<JsonElement, UserBean>() {
            @Override
            public UserBean call(JsonElement jsonElement) {
                return new Gson().fromJson(jsonElement, UserBean.class);
            }
        });
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
        String url = ApiUrl.SERVER_CHECK + "?access_token=" + mAccessToken.accessToken;
        mFastVolley.cancelAll(url);
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, requestFuture, requestFuture);
        request.setTag(url);
        mFastVolley.addShortRequest(HASHCODE, request);
        try {
            JSONObject obj = requestFuture.get();
            int errcode = obj.optInt("errcode", -1);
            if (errcode == ServerErrorCode.SUCCESS) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "checkAccessToken " + e.getClass() + ":" + e.getMessage());
            return false;
        }
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
    public void favor(String gid) throws Exception {
        innerFavor(gid, true);
    }

    @Override
    public void unFavor(String gid) throws Exception {
        innerFavor(gid, false);
    }

    private void innerFavor(String gid, boolean bFavor) throws Exception {
        String url = bFavor ? ApiUrl.SERVER_FAVOR + "?access_token=" + mAccessToken.accessToken :
                ApiUrl.SERVER_UNFAVOR + "?access_token=" + mAccessToken.accessToken;
        mFastVolley.cancelAll(url);
        JSONObject reqParams = new JSONObject().put("gid", gid);
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, reqParams, requestFuture, requestFuture);
        request.setTag(url);
        mFastVolley.addShortRequest(HASHCODE, request);
        JSONObject obj = requestFuture.get();
        int errcode = obj.optInt("errcode", -1);
        if (errcode != ServerErrorCode.SUCCESS) {
            throw new StockSystemException(obj.optString("errmsg"));
        }
    }

    @Override
    public Observable<List<String>> listFavor() {
        return Observable.create(new Observable.OnSubscribe<JSONArray>() {
            @Override
            public void call(Subscriber<? super JSONArray> subscriber) {
                String url = ApiUrl.SERVER_LIST_FAVOR + "?access_token=" + mAccessToken.accessToken;
                mFastVolley.cancelAll(url);
                RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
                JsonObjectRequest request = new JsonObjectRequest(url, requestFuture, requestFuture);
                request.setTag(url);
                mFastVolley.addDefaultRequest(HASHCODE, request);
                try {
                    JSONObject obj = requestFuture.get();
                    int errcode = obj.optInt("errcode", -1);
                    if (errcode == ServerErrorCode.SUCCESS && obj.has("data")) {
                        subscriber.onNext(obj.getJSONArray("data"));
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new StockSystemException(obj.optString("errmsg")));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "listFavor " + e.getClass() + ":" + e.getMessage());
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<JSONArray, List<String>>() {
            @Override
            public List<String> call(JSONArray jsonArray) {
                List<String> result = new ArrayList<>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    result.add(jsonArray.optJSONObject(i).optString("gid"));
                }
                return result;
            }
        });
    }

    @Override
    public Observable<AccountBean> getAccount() {
        return Observable.create(new Observable.OnSubscribe<JsonElement>() {
            @Override
            public void call(Subscriber<? super JsonElement> subscriber) {
                String url = ApiUrl.SERVER_GET_ACCOUNT + "?access_token=" + mAccessToken.accessToken;
                mFastVolley.cancelAll(url);
                RequestFuture<JsonObject> requestFuture = RequestFuture.newFuture();
                GsonObjectRequest request = new GsonObjectRequest(url, requestFuture, requestFuture);
                request.setTag(url);
                mFastVolley.addDefaultRequest(HASHCODE, request);
                try {
                    JsonObject obj = requestFuture.get();
                    int errcode = obj.get("errcode").getAsInt();
                    if (errcode == ServerErrorCode.SUCCESS && obj.has("data")) {
                        subscriber.onNext(obj.get("data"));
                        subscriber.onCompleted();
                    } else if (errcode == ServerErrorCode.ACCESS_TOKEN_EXPIRES) {
                        subscriber.onError(new StockSystemException(obj.get("errmsg").getAsString()));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getAccount " + e.getClass() + ":" + e.getMessage());
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<JsonElement, AccountBean>() {
            @Override
            public AccountBean call(JsonElement jsonElement) {
                return new Gson().fromJson(jsonElement, AccountBean.class);
            }
        });
    }

    @Override
    public void bindAccount(AccountBean accountBean) throws Exception {
        String url = ApiUrl.SERVER_BIND_ACCOUNT + "?access_token=" + mAccessToken.accessToken;
        mFastVolley.cancelAll(url);
        String reqBody = new Gson().toJson(accountBean);
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqBody, requestFuture, requestFuture);
        request.setTag(url);
        mFastVolley.addShortRequest(HASHCODE, request);
        JSONObject obj = requestFuture.get();
        int errcode = obj.optInt("errcode", -1);
        if (errcode != ServerErrorCode.SUCCESS) {
            throw new StockSystemException(obj.optString("errmsg"));
        }
    }

    @Override
    public void recharge(String cardNum, String password, float money) throws Exception {
        String url = ApiUrl.SERVER_RECHARGE + "?access_token=" + mAccessToken.accessToken;
        mFastVolley.cancelAll(url);
        JSONObject reqParams = new JSONObject();
        reqParams.put("cardNum", cardNum);
        reqParams.put("password", password);
        reqParams.put("money", money);
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqParams, requestFuture, requestFuture);
        request.setTag(url);
        mFastVolley.addShortRequest(HASHCODE, request);
        JSONObject obj = requestFuture.get();
        int errcode = obj.optInt("errcode", -1);
        if (errcode != ServerErrorCode.SUCCESS) {
            throw new StockSystemException(obj.optString("errmsg"));
        }
    }

    @Override
    public Observable<List<TradeBean>> listTrade() {
        return Observable.create(new Observable.OnSubscribe<JsonArray>() {
            @Override
            public void call(Subscriber<? super JsonArray> subscriber) {
                String url = ApiUrl.SERVER_LIST_TRADE + "?access_token=" + mAccessToken.accessToken;
                mFastVolley.cancelAll(url);
                RequestFuture<JsonObject> requestFuture = RequestFuture.newFuture();
                GsonObjectRequest request = new GsonObjectRequest(url, requestFuture, requestFuture);
                request.setTag(url);
                mFastVolley.addDefaultRequest(HASHCODE, request);
                try {
                    JsonObject obj = requestFuture.get();
                    int errcode = obj.get("errcode").getAsInt();
                    if (errcode == ServerErrorCode.SUCCESS && obj.has("data")) {
                        subscriber.onNext(obj.get("data").getAsJsonArray());
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new StockSystemException(obj.get("errmsg").getAsString()));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "listFavor " + e.getClass() + ":" + e.getMessage());
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<JsonArray, List<TradeBean>>() {
            @Override
            public List<TradeBean> call(JsonArray jsonArray) {
                List<TradeBean> beans = new ArrayList<>(jsonArray.size());
                for (int i = 0; i < jsonArray.size(); i++) {
                    TradeBean stockBean = new Gson().fromJson(jsonArray.get(i), TradeBean.class);
                    beans.add(stockBean);
                }
                return beans;
            }
        });
    }

    @Override
    public void buy(String gid, String name, float uPrice, int amount) throws Exception {
        innerTrade(gid, name, uPrice, amount, false);
    }

    @Override
    public void sell(String gid, String name, float uPrice, int amount) throws Exception {
        innerTrade(gid, name, uPrice, amount, true);
    }

    private void innerTrade(String gid, String name, float uPrice, int amount, boolean bSell) throws Exception {
        String url = bSell ? ApiUrl.SERVER_SELL + "?access_token=" + mAccessToken.accessToken :
                ApiUrl.SERVER_BUY + "?access_token=" + mAccessToken.accessToken;
        mFastVolley.cancelAll(url);
        JSONObject reqParams = new JSONObject();
        reqParams.put("gid", gid);
        reqParams.put("name", name);
        reqParams.put("uPrice", uPrice);
        reqParams.put("amount", amount);
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqParams, requestFuture, requestFuture);
        request.setTag(url);
        mFastVolley.addShortRequest(HASHCODE, request);
        JSONObject obj = requestFuture.get();
        int errcode = obj.optInt("errcode", -1);
        if (errcode != ServerErrorCode.SUCCESS) {
            throw new StockSystemException(obj.optString("errmsg"));
        }
    }

    @Override
    public Observable<List<HoldStockBean>> listHoldStock() {
        return Observable.create(new Observable.OnSubscribe<JsonArray>() {
            @Override
            public void call(Subscriber<? super JsonArray> subscriber) {
                String url = ApiUrl.SERVER_LIST_STOCK + "?access_token=" + mAccessToken.accessToken;
                mFastVolley.cancelAll(url);
                RequestFuture<JsonObject> requestFuture = RequestFuture.newFuture();
                GsonObjectRequest request = new GsonObjectRequest(url, requestFuture, requestFuture);
                request.setTag(url);
                mFastVolley.addDefaultRequest(HASHCODE, request);
                try {
                    JsonObject obj = requestFuture.get();
                    int errcode = obj.get("errcode").getAsInt();
                    if (errcode == ServerErrorCode.SUCCESS && obj.has("data")) {
                        subscriber.onNext(obj.get("data").getAsJsonArray());
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new StockSystemException(obj.get("errmsg").getAsString()));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "listFavor " + e.getClass() + ":" + e.getMessage());
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<JsonArray, List<HoldStockBean>>() {
            @Override
            public List<HoldStockBean> call(JsonArray jsonArray) {
                List<HoldStockBean> beans = new ArrayList<>(jsonArray.size());
                for (int i = 0; i < jsonArray.size(); i++) {
                    HoldStockBean stockBean = new Gson().fromJson(jsonArray.get(i), HoldStockBean.class);
                    beans.add(stockBean);
                }
                return beans;
            }
        });
    }

    public void destroy() {
        mFastVolley.cancelAll(HASHCODE);
        mFastVolley = null;
    }

}
