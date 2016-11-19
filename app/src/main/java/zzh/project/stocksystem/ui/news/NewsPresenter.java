package zzh.project.stocksystem.ui.news;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zzh.project.stocksystem.bean.NewsBean;
import zzh.project.stocksystem.helper.MsgHelper;
import zzh.project.stocksystem.model.NewsManager;
import zzh.project.stocksystem.model.impl.NewsManagerImpl;
import zzh.project.stocksystem.ui.base.BasePresenter;

public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {
    private NewsManager mNewsManager;
    private NewsManager.OnNewsChangedListener mNewsChangedListener = new NewsManager.OnNewsChangedListener() {
        @Override
        public void onNewsChanged() {
            loadNews(false);
        }
    };

    public NewsPresenter(NewsContract.View view) {
        super(view);
        mNewsManager = NewsManagerImpl.getInstance();
        mNewsManager.register(mNewsChangedListener);
    }

    @Override
    public void doFirst() {
        loadNews(false);
    }

    @Override
    public void loadNews(final boolean manual) {
        if (manual) {
            mView.setLoadingIndicator(true);
        }
        mSubscription.clear();
        Subscription subscription = mNewsManager.listNews().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<NewsBean>>() {
            @Override
            public void onCompleted() {
                if (manual && mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mView != null && mView.isActive()) {
                    if (manual) {
                        mView.setLoadingIndicator(false);
                    }
                    mView.showErrorMessage(MsgHelper.getErrorMsg(e));
                }
            }

            @Override
            public void onNext(List<NewsBean> newsBeen) {
                if (mView != null && mView.isActive()) {
                    if (manual) {
                        mView.setLoadingIndicator(false);
                    }
                    mView.showNews(newsBeen);
                }
            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void destroy() {
        mNewsManager.unregister(mNewsChangedListener);
        super.destroy();
    }
}
