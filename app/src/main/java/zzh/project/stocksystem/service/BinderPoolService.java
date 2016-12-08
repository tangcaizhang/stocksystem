package zzh.project.stocksystem.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import zzh.project.stocksystem.aidl.IBinderPool;

/**
 * 服务端暴露的BinderPoolService
 */
public class BinderPoolService extends Service {
    public static class BinderPoolImpl extends IBinderPool.Stub {
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                // case ... 根据binderCode返回不同的实现类
            }
            return binder;
        }
    }

    private Binder mBinderPool = new BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }
}
