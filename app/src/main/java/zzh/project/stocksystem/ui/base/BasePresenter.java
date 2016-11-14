package zzh.project.stocksystem.ui.base;

import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends IView> implements IPresenter {
    protected final String TAG = this.getClass().getSimpleName();
    protected CompositeSubscription mSubscription;
    protected T mView;

    public BasePresenter(T view) {
        mView = view;
        mSubscription = new CompositeSubscription();
    }

    public abstract void doFirst();

    @Override
    public void subscribe() {
        doFirst();
    }

    @Override
    public void unsubscribe() {
        mSubscription.clear();
    }

    @Override
    public void destroy() {
        mSubscription.unsubscribe();
        mView = null;
    }
}
