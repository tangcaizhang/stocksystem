package zzh.project.stocksystem;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
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
import zzh.project.stocksystem.model.impl.SettingsManagerImpl;
import zzh.project.stocksystem.model.impl.StockManagerJuheImpl;
import zzh.project.stocksystem.model.impl.UserManagerImpl;
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

        // 初始化检测，避免多进程重复加载第三方类库
        if (!checkProcess(this)) {
            return;
        }


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
        if (SettingsManagerImpl.getInstance().isEnablePush()) {
            SettingsManagerImpl.getInstance().enablePush();
        } else {
            SettingsManagerImpl.getInstance().disablePush();
        }
    }

    private boolean checkProcess(Context context) {
        ActivityManager actMgr = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        int pid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo appProcess : actMgr.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
               String name = appProcess.processName;
                if (name.equals(getPackageName())) {
                    return true;
                }
            }
        }
        return false;
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

//        if (!EnvConst.DEBUG) {
            L.disableLogging();
//        }
    }

    /********************** 初始化jpush ***************/
    private void initJPush(Context context) {
        JPushInterface.setDebugMode(EnvConst.DEBUG);
        JPushInterface.init(context);
    }

    /********************** 初始化全局异常捕获 ***************/
    private void initGlobalUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // TODO save log
                e.printStackTrace();
                destroy();
            }
        });
    }

    // 回收资源
    public void destroy() {
        if (!checkProcess(this)) {
            return;
        }

        if (EnvConst.DEBUG) {
            Log.d(TAG, "destroy");
        }
        if (mNetWorkObservable != null) {
            mNetWorkObservable.release();
        }
        UserManagerImpl.getInstance().destroy();
        StockManagerJuheImpl.getInstance().destroy();
        ImageLoader.getInstance().destroy();
        FastVolley.getInstance(this).stop();
        OpenHelperManager.releaseHelper();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
