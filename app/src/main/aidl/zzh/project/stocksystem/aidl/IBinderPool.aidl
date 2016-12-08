// IBinderPool.aidl
package zzh.project.stocksystem.aidl;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
