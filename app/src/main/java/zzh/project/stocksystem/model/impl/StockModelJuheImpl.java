package zzh.project.stocksystem.model.impl;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.ApiUrl;
import zzh.project.stocksystem.MyApplication;
import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.model.StockModel;
import zzh.project.stocksystem.model.exception.StockSystemException;
import zzh.project.stocksystem.model.impl.juhe.JuheDetailRespHK;
import zzh.project.stocksystem.model.impl.juhe.JuheDetailRespHS;
import zzh.project.stocksystem.model.impl.juhe.JuheDetailRespUSA;
import zzh.project.stocksystem.model.impl.juhe.JuheListRespHK;
import zzh.project.stocksystem.model.impl.juhe.JuheListRespSH;
import zzh.project.stocksystem.model.impl.juhe.JuheListRespSZ;
import zzh.project.stocksystem.model.impl.juhe.JuheListRespUSA;
import zzh.project.stocksystem.volley.FastVolley;
import zzh.project.stocksystem.volley.GsonObjectRequest;

public class StockModelJuheImpl implements StockModel {
    private final String TAG = this.getClass().getSimpleName();
    private static StockModelJuheImpl sInstance;
    private FastVolley mFastVolley;
    private Gson mGson;
    private String HASHCODE;
    private String mAppKey;

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
    public Observable<StockDetailBean> getDetail(final String gid) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                String url;
                Class respClazz;
                if (gid.startsWith("sh") || gid.startsWith("sz")) {
                    url = ApiUrl.STOCK_DETAIL_HS + "?gid=" + gid + "&key=" + mAppKey;
                    respClazz = JuheDetailRespHS.class;
                } else if (gid.startsWith("hk")) {
                    url = ApiUrl.STOCK_DETAIL_HK + "?num=" + gid.substring(2) + "&key=" + mAppKey;
                    respClazz = JuheDetailRespHK.class;
                } else {
                    url = ApiUrl.STOCK_DETAIL_USA + "?gid=" + gid + "&key=" + mAppKey;
                    respClazz = JuheDetailRespUSA.class;
                }
                RequestFuture<JsonObject> requestFuture = RequestFuture.newFuture();
                GsonObjectRequest request = new GsonObjectRequest(url, requestFuture, requestFuture);
                request.setTag(url);
                mFastVolley.addDefaultRequest(HASHCODE, request);
                try {
                    JsonObject obj = requestFuture.get();
                    subscriber.onNext(mGson.fromJson(obj, respClazz));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, "getDetail " + e.getClass() + ":" + e.getMessage());
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).map(new Func1<Object, StockDetailBean>() {
            @Override
            public StockDetailBean call(Object o) {
                return convert(o);
            }
        });
    }

    @Override
    public Observable<List<StockBean>> findAllHK(final int page) {
        String url = ApiUrl.STOCK_LIST_HK + "?key=" + mAppKey + "&page=" + page;
        return findAll(url, JuheListRespHK.class, JuheListRespHK.Result.Data.class, new Func1<JuheListRespHK, Observable<JuheListRespHK.Result.Data>>() {
            @Override
            public Observable<JuheListRespHK.Result.Data> call(JuheListRespHK juheListRespSH) {
                return Observable.from(juheListRespSH.result.data);
            }
        }, new Func1<JuheListRespHK.Result.Data, StockBean>() {
            @Override
            public StockBean call(JuheListRespHK.Result.Data d) {
                StockBean bean = new StockBean();
                bean.gid = "hk" + d.symbol;
                bean.name = d.name;
                bean.nowPri = d.lasttrade;
                bean.increase = d.pricechange;
                bean.increPer = d.changepercent;
                bean.thumbUrl = "http://image.sinajs.cn/newchart/hk_stock/daily/" + bean.gid + ".gif";
                return bean;
            }
        });
    }

    @Override
    public Observable<List<StockBean>> findAllUS(int page) {
        String url = ApiUrl.STOCK_LIST_US + "?key=" + mAppKey + "&page=" + page;
        return findAll(url, JuheListRespUSA.class, JuheListRespUSA.Result.Data.class, new Func1<JuheListRespUSA, Observable<JuheListRespUSA.Result.Data>>() {
            @Override
            public Observable<JuheListRespUSA.Result.Data> call(JuheListRespUSA juheListRespSH) {
                return Observable.from(juheListRespSH.result.data);
            }
        }, new Func1<JuheListRespUSA.Result.Data, StockBean>() {
            @Override
            public StockBean call(JuheListRespUSA.Result.Data d) {
                StockBean bean = new StockBean();
                bean.gid = d.symbol;
                bean.name = d.cname;
                bean.nowPri = d.price;
                bean.increase = d.diff;
                bean.increPer = d.chg;
                bean.thumbUrl = "http://image.sinajs.cn/newchartv5/usstock/daily/" + bean.gid + ".gif";
                return bean;
            }
        });
    }

    @Override
    public Observable<List<StockBean>> findAllSZ(int page) {
        String url = ApiUrl.STOCK_LIST_SZ + "?key=" + mAppKey + "&page=" + page;
        return findAll(url, JuheListRespSZ.class, JuheListRespSZ.Result.Data.class, new Func1<JuheListRespSZ, Observable<JuheListRespSZ.Result.Data>>() {
            @Override
            public Observable<JuheListRespSZ.Result.Data> call(JuheListRespSZ juheListRespSH) {
                return Observable.from(juheListRespSH.result.data);
            }
        }, new Func1<JuheListRespSZ.Result.Data, StockBean>() {
            @Override
            public StockBean call(JuheListRespSZ.Result.Data d) {
                StockBean bean = new StockBean();
                bean.gid = d.symbol;
                bean.name = d.name;
                bean.nowPri = d.trade;
                bean.increase = d.pricechange;
                bean.increPer = d.changepercent;
                bean.thumbUrl = "http://image.sinajs.cn/newchart/daily/n/" + bean.gid + ".gif";
                return bean;
            }
        });
    }

    @Override
    public Observable<List<StockBean>> findAllSH(int page) {
        String url = ApiUrl.STOCK_LIST_SH + "?key=" + mAppKey + "&page=" + page;
        return findAll(url, JuheListRespSH.class, JuheListRespSH.Result.Data.class, new Func1<JuheListRespSH, Observable<JuheListRespSH.Result.Data>>() {
            @Override
            public Observable<JuheListRespSH.Result.Data> call(JuheListRespSH juheListRespSH) {
                return Observable.from(juheListRespSH.result.data);
            }
        }, new Func1<JuheListRespSH.Result.Data, StockBean>() {
            @Override
            public StockBean call(JuheListRespSH.Result.Data d) {
                StockBean bean = new StockBean();
                bean.gid = d.symbol;
                bean.name = d.name;
                bean.nowPri = d.trade;
                bean.increase = d.pricechange;
                bean.increPer = d.changepercent;
                bean.thumbUrl = "http://image.sinajs.cn/newchart/daily/n/" + bean.gid + ".gif";
                return bean;
            }
        });
    }

    private <T1, T2> Observable<List<StockBean>> findAll(final String url, final Class<T1> respClazz, Class<T2> subDataClazz,
                                                         Func1<T1, Observable<T2>> flatMap, Func1<T2, StockBean> map) {
        return Observable.create(new Observable.OnSubscribe<T1>() {
            @Override
            public void call(Subscriber<? super T1> subscriber) {
                RequestFuture<JsonObject> requestFuture = RequestFuture.newFuture();
                GsonObjectRequest request = new GsonObjectRequest(url, requestFuture, requestFuture);
                request.setTag(url);
                mFastVolley.addDefaultRequest(HASHCODE, request);
                try {
                    JsonObject obj = requestFuture.get();
                    // 先检测网络响应在用gson转换，否则字段匹配不上
                    if (obj.has("error_code")) {
                        int err_code = obj.get("error_code").getAsInt();
                        if (err_code == 0) {
                            subscriber.onNext(mGson.fromJson(obj, respClazz));
                            subscriber.onCompleted();
                        } else if (err_code == 202102) {
                            subscriber.onNext(null);
                        } else {
                            subscriber.onError(new Exception("unknow"));
                        }
                    } else {
                        subscriber.onError(new StockSystemException("error_code has been lose"));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "findAll " + e.getClass() + ":" + e.getMessage());
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).flatMap(flatMap).map(map).toList();
    }

    // 从网络响应的bean转成视图用的vo
    private StockDetailBean convert(final Object obj) {
        StockDetailBean bean = null;
        try {
            if (obj instanceof JuheDetailRespHS) {
                JuheDetailRespHS resp = (JuheDetailRespHS) obj;
                if (resp.resultcode.equals("200")) {
                    bean = new StockDetailBean();
                    bean.name = resp.result.get(0).data.name;
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
                    bean.name = resp.result.get(0).data.name;
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
                    bean.name = resp.result.get(0).data.name;
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
            Log.e(TAG, "convert " + e.getClass() + ":" + e.getMessage());
        }
        return bean;
    }

    public void destroy() {
        mFastVolley.cancelAll(HASHCODE);
        mFastVolley = null;
    }
}
