package zzh.project.stocksystem.util;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

import zzh.project.stocksystem.aidl.IBinderPool;

/**
 * AIDL辅助工具，实现BinderPool机制，远端只需要暴露一个Service即可提供不同的aidl接口服务
 */
public class BinderPool {
    private static BinderPool sInstance;
    private Context mContext;
    private CountDownLatch mConnectBinderPoolCountDownLatch;
    private Intent mTargetService;
    private IBinderPool mBinderPool;

    private BinderPool(Context context, Intent targetService) {
        mContext = context;
        mTargetService = targetService;
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context, Intent targetService) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {
                    sInstance = new BinderPool(context, targetService);
                }
            }
        }
        return sInstance;
    }

    private synchronized void connectBinderPoolService() {
        // 创建一个CountDownLatch，使当前线程阻塞直到bindService成功
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        mContext.bindService(mTargetService, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinderPool = IBinderPool.Stub.asInterface(iBinder);
            try {
                // 注册监听器，监听binder断开
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            // 断开时重连
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    // 根据binderCode获取不同的远程Binder
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }
}
