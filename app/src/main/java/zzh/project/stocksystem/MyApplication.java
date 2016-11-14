package zzh.project.stocksystem;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import zzh.project.stocksystem.helper.JPushHelper;
import zzh.project.stocksystem.model.impl.SettingsModelImpl;
import zzh.project.stocksystem.model.impl.StockModelJuheImpl;
import zzh.project.stocksystem.model.impl.UserModelImpl;
import zzh.project.stocksystem.volley.FastVolley;

public class MyApplication extends Application {
    private final String TAG = this.getClass().getSimpleName();
    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (EnvConst.DEBUG) {
            Log.d(TAG, "onCreate");
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        initNetWorkStatusObservable();
        initImageLoader(this);
        initJPush(this);
        initGlobalUncaughtExceptionHandler();

        // 初始设置项
        if (SettingsModelImpl.getInstance().isEnablePush()) {
            SettingsModelImpl.getInstance().enablePush();
        } else {
            SettingsModelImpl.getInstance().disablePush();
        }
    }

    private NetWorkObservable mNetWorkObservable;

    /********************** 初始化网络监听 ***************/
    private void initNetWorkStatusObservable() {
        mNetWorkObservable = new NetWorkObservable(this);
    }

    public boolean isNetworkActive() {
        if (mNetWorkObservable != null) {
            return mNetWorkObservable.isNetworkActive();
        }
        return true;
    }

    public void registerNetWorkObserver(NetWorkObservable.NetWorkObserver observer) {
        if (mNetWorkObservable != null) {
            mNetWorkObservable.registerObserver(observer);
        }
    }

    public void unregisterNetWorkObserver(NetWorkObservable.NetWorkObserver observer) {
        if (mNetWorkObservable != null) {
            mNetWorkObservable.unregisterObserver(observer);
        }
    }

    /******************** 初始化图片加载**********************/
    private void initImageLoader(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCacheAware<String, Bitmap> memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), context.getPackageName() + "/img_cache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize((int) (Runtime.getRuntime().maxMemory() / 5)) //设置内存缓存的大小
                .discCache(new TotalSizeLimitedDiscCache(cacheDir, 50 * 1024 * 1024)) // 最多缓存50M的图片
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).memoryCache(memoryCache).tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(4)
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        if (!EnvConst.DEBUG) {
            L.disableLogging();
        }
    }

    /********************** 初始化jpush ***************/
    private void initJPush(Context context) {
        JPushInterface.setDebugMode(EnvConst.DEBUG);
        JPushInterface.init(context);
        // 修改别名，防止收到推送
        JPushHelper.setAlias("null");
    }

    /********************** 初始化全局异常捕获 ***************/
    private void initGlobalUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // TODO save log
                destroy();
            }
        });
    }

    // 回收资源
    public void destroy() {
        if (EnvConst.DEBUG) {
            Log.d(TAG, "destroy");
        }
        if (mNetWorkObservable != null) {
            mNetWorkObservable.release();
        }
        UserModelImpl.getInstance().destroy();
        StockModelJuheImpl.getInstance().destroy();
        ImageLoader.getInstance().destroy();
        FastVolley.getInstance(this).stop();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
