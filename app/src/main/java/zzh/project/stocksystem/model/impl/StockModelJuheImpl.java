package zzh.project.stocksystem.model.impl;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import zzh.project.stocksystem.ApiUrl;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.Callback1;
import zzh.project.stocksystem.model.Callback2;
import zzh.project.stocksystem.model.StockModel;
import zzh.project.stocksystem.util.ThreadManager;
import zzh.project.stocksystem.volley.FastVolley;
import zzh.project.stocksystem.volley.GsonObjectRequest;

public class StockModelJuheImpl implements StockModel {
    public static final String TAG = UserModelImpl.class.getSimpleName();
    private static StockModelJuheImpl sInstance;
    private FastVolley mFastVolley;
    private Gson mGson;
    private String HASHCODE;
    private String mAppKey;
    private Handler mHandler = new Handler();

    private StockModelJuheImpl(Context context) {
        HASHCODE = Integer.toHexString(this.hashCode()) + "@";
        mFastVolley = FastVolley.getInstance(context);
        mGson = new Gson();
        try {
            mAppKey = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString("juhe_app_key");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static StockModelJuheImpl getInstance() {
        if (sInstance == null) {
            sInstance = new StockModelJuheImpl(MyApplication.getInstance());
        }
        return sInstance;
    }

    @Override
    public void getDetail(String gid, final Callback2<StockDetailBean, String> callback) {
        String url = null;
        Class respClazz = null;
        if (gid.startsWith("hs")) {
            url = ApiUrl.STOCK_DETAIL_HS + "?gid=" + gid + "&key=" + mAppKey;
            respClazz = JuheDetailRespHS.class;
        } else if (gid.startsWith("hk")) {
            url = ApiUrl.STOCK_DETAIL_HK + "?num=" + gid.substring(2) + "&key=" + mAppKey;
            respClazz = JuheDetailRespHK.class;
        } else {
            url = ApiUrl.STOCK_DETAIL_USA + "?gid=" + gid + "&key=" + mAppKey;
            respClazz = JuheDetailRespUSA.class;
        }
        final Class finalRespClazz = respClazz;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Object resp = mGson.fromJson(response, finalRespClazz);
                convert(resp, new Callback1<StockDetailBean>() {
                    @Override
                    public void onCallback(StockDetailBean stockDetailBean) {
                        callback.onSuccess(stockDetailBean);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("detail");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    private void convert(final Object obj, final Callback1<StockDetailBean> callback) {
        ThreadManager.getPool().execute(new Runnable() {
            @Override
            public void run() {
                StockDetailBean bean = null;
                try {
                    if (obj instanceof JuheDetailRespHS) {
                        JuheDetailRespHS resp = (JuheDetailRespHS) obj;
                        if (resp.resultcode.equals("200")) {
                            bean = new StockDetailBean();
                            bean.gid = resp.result.get(0).data.gid;
                            bean.increPer = resp.result.get(0).data.increPer;
                            bean.increase = resp.result.get(0).data.increase;
                            bean.todayStartPri = resp.result.get(0).data.todayStartPri;
                            bean.yestodEndPri = resp.result.get(0).data.yestodEndPri;
                            bean.nowPri = resp.result.get(0).data.nowPri;
                            bean.todayMax = resp.result.get(0).data.todayMax;
                            bean.todayMin = resp.result.get(0).data.todayMin;
                            bean.competitivePri = resp.result.get(0).data.competitivePri;
                            bean.reservePri = resp.result.get(0).data.reservePri;
                            bean.traNumber = resp.result.get(0).data.traNumber;
                            bean.traAmount = resp.result.get(0).data.traAmount;
                            bean.minUrl = resp.result.get(0).gopicture.minurl;
                            bean.dayUrl = resp.result.get(0).gopicture.dayurl;
                            bean.weekUrl = resp.result.get(0).gopicture.weekurl;
                            bean.monthUrl = resp.result.get(0).gopicture.monthurl;
                        }
                    } else if (obj instanceof JuheDetailRespHK) {
                        JuheDetailRespHK resp = (JuheDetailRespHK) obj;
                        if (resp.resultcode.equals("200")) {
                            bean = new StockDetailBean();
                            bean.gid = resp.result.get(0).data.gid;
                            bean.increPer = resp.result.get(0).data.limit;
                            bean.increase = resp.result.get(0).data.uppic;
                            bean.todayStartPri = resp.result.get(0).data.openpri;
                            bean.yestodEndPri = resp.result.get(0).data.formpri;
                            bean.nowPri = resp.result.get(0).data.openpri;
                            bean.todayMax = resp.result.get(0).data.maxpri;
                            bean.todayMin = resp.result.get(0).data.minpri;
                            bean.competitivePri = resp.result.get(0).data.inpic;
                            bean.reservePri = resp.result.get(0).data.outpic;
                            bean.traNumber = resp.result.get(0).data.traNumber;
                            bean.traAmount = resp.result.get(0).data.traAmount;
                            bean.minUrl = resp.result.get(0).gopicture.minurl;
                            bean.dayUrl = resp.result.get(0).gopicture.dayurl;
                            bean.weekUrl = resp.result.get(0).gopicture.weekurl;
                            bean.monthUrl = resp.result.get(0).gopicture.monthurl;
                        }
                    } else if (obj instanceof JuheDetailRespUSA) {
                        JuheDetailRespUSA resp = (JuheDetailRespUSA) obj;
                        if (resp.resultcode.equals("200")) {
                            bean = new StockDetailBean();
                            bean.gid = resp.result.get(0).data.gid;
                            bean.increPer = resp.result.get(0).data.limit;
                            bean.increase = resp.result.get(0).data.uppic;
                            bean.todayStartPri = resp.result.get(0).data.openpri;
                            bean.yestodEndPri = resp.result.get(0).data.formpri;
                            bean.nowPri = resp.result.get(0).data.openpri;
                            bean.todayMax = resp.result.get(0).data.maxpri;
                            bean.todayMin = resp.result.get(0).data.minpri;
                            bean.competitivePri = null;
                            bean.reservePri = null;
                            bean.traNumber = resp.result.get(0).data.avgTraNumber;
                            bean.traAmount = resp.result.get(0).data.traAmount;
                            bean.minUrl = resp.result.get(0).gopicture.minurl;
                            bean.dayUrl = resp.result.get(0).gopicture.dayurl;
                            bean.weekUrl = resp.result.get(0).gopicture.weekurl;
                            bean.monthUrl = resp.result.get(0).gopicture.monthurl;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final StockDetailBean tmpRefBean = bean;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCallback(tmpRefBean);
                    }
                });
            }
        });
    }

    @Override
    public void findAllHK(int page, final Callback2<List<StockBean>, String> callback) {
        String url = ApiUrl.STOCK_LIST_HK + "?key=" + mAppKey + "&page=" + page;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (!preParserCheck(response, callback)) {
                    return;
                }
                JuheListRespHK resp = mGson.fromJson(response, JuheListRespHK.class);
                convert(resp, new Callback1<List<StockBean>>() {
                    @Override
                    public void onCallback(List<StockBean> stockBeen) {
                        callback.onSuccess(stockBeen);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("findAllHK");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    private void convert(final JuheListRespHK resp, final Callback1<List<StockBean>> callback) {
        ThreadManager.getPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<StockBean> data = new ArrayList<StockBean>(20);
                try {
                    List<JuheListRespHK.Result.Data> datas = resp.result.data;
                    for (JuheListRespHK.Result.Data d : datas) {
                        StockBean bean = new StockBean();
                        bean.gid = "hk" + d.symbol;
                        bean.name = d.name;
                        bean.nowPri = d.lasttrade;
                        bean.increase = d.pricechange;
                        bean.increPer = d.changepercent;
                        bean.thumbUrl = "http://image.sinajs.cn/newchart/hk_stock/daily/" + bean.gid + ".gif";
                        data.add(bean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCallback(data);
                    }
                });
            }
        });
    }

    @Override
    public void findAllUS(int page, final Callback2<List<StockBean>, String> callback) {
        String url = ApiUrl.STOCK_LIST_US + "?key=" + mAppKey + "&page=" + page;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (!preParserCheck(response, callback)) {
                    return;
                }
                JuheListRespUSA resp = mGson.fromJson(response, JuheListRespUSA.class);
                convert(resp, new Callback1<List<StockBean>>() {
                    @Override
                    public void onCallback(List<StockBean> stockBeen) {
                        callback.onSuccess(stockBeen);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("findAllUS");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    private void convert(final JuheListRespUSA resp, final Callback1<List<StockBean>> callback) {
        ThreadManager.getPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<StockBean> data = new ArrayList<StockBean>(20);
                try {
                    List<JuheListRespUSA.Result.Data> datas = resp.result.data;
                    for (JuheListRespUSA.Result.Data d : datas) {
                        StockBean bean = new StockBean();
                        bean.gid = d.symbol;
                        bean.name = d.cname;
                        bean.nowPri = d.price;
                        bean.increase = d.diff;
                        bean.increPer = d.chg;
                        bean.thumbUrl = "http://image.sinajs.cn/newchartv5/usstock/daily/" + bean.gid + ".gif";
                        data.add(bean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCallback(data);
                    }
                });
            }
        });
    }

    @Override
    public void findAllSZ(int page, final Callback2<List<StockBean>, String> callback) {
        String url = ApiUrl.STOCK_LIST_SZ + "?key=" + mAppKey + "&page=" + page;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (!preParserCheck(response, callback)) {
                    return;
                }
                JuheListRespSZ resp = mGson.fromJson(response, JuheListRespSZ.class);
                convert(resp, new Callback1<List<StockBean>>() {
                    @Override
                    public void onCallback(List<StockBean> stockBeen) {
                        callback.onSuccess(stockBeen);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("findAllSZ");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    private void convert(final JuheListRespSZ resp, final Callback1<List<StockBean>> callback) {
        ThreadManager.getPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<StockBean> data = new ArrayList<StockBean>(20);
                try {
                    List<JuheListRespSZ.Result.Data> datas = resp.result.data;
                    for (JuheListRespSZ.Result.Data d : datas) {
                        StockBean bean = new StockBean();
                        bean.gid = d.symbol;
                        bean.name = d.name;
                        bean.nowPri = d.trade;
                        bean.increase = d.pricechange;
                        bean.increPer = d.changepercent;
                        bean.thumbUrl = "http://image.sinajs.cn/newchart/daily/n/" + bean.gid + ".gif";
                        data.add(bean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCallback(data);
                    }
                });
            }
        });
    }

    @Override
    public void findAllSH(int page, final Callback2<List<StockBean>, String> callback) {
        String url = ApiUrl.STOCK_LIST_SH + "?key=" + mAppKey + "&page=" + page;
        GsonObjectRequest request = new GsonObjectRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (!preParserCheck(response, callback)) {
                    return;
                }
                JuheListRespSH resp = mGson.fromJson(response, JuheListRespSH.class);
                convert(resp, new Callback1<List<StockBean>>() {
                    @Override
                    public void onCallback(List<StockBean> stockBeen) {
                        callback.onSuccess(stockBeen);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(MsgHelper.getErrorMsg(error));
            }
        });
        request.setTag("findAllSH");
        mFastVolley.addDefaultRequest(HASHCODE, request);
    }

    private void convert(final JuheListRespSH resp, final Callback1<List<StockBean>> callback) {
        ThreadManager.getPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<StockBean> data = new ArrayList<StockBean>(20);
                try {
                    List<JuheListRespSH.Result.Data> datas = resp.result.data;
                    for (JuheListRespSH.Result.Data d : datas) {
                        StockBean bean = new StockBean();
                        bean.gid = d.symbol;
                        bean.name = d.name;
                        bean.nowPri = d.trade;
                        bean.increase = d.pricechange;
                        bean.increPer = d.changepercent;
                        bean.thumbUrl = "http://image.sinajs.cn/newchart/daily/n/" + bean.gid + ".gif";
                        data.add(bean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCallback(data);
                    }
                });
            }
        });
    }

    // 转换前预检测，聚合api错误时返回的结构体不一样
    private boolean preParserCheck(JsonObject data, Callback2<List<StockBean>, String> callback) {
        if (data.has("error_code")) {
            int err_code = data.get("error_code").getAsInt();
            if (err_code == 0) {
                return true;
            }
            if (err_code == 202102) {
                callback.onSuccess(null);
            }
        }
        return false;
    }

    public void destroy() {
        mFastVolley.cancelAll(HASHCODE);
    }
}
