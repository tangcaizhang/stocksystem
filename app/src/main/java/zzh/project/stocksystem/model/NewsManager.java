package zzh.project.stocksystem.model;

import java.util.List;

import rx.Observable;
import zzh.project.stocksystem.bean.NewsBean;

public interface NewsManager {
    interface OnNewsChangedListener {
        void onNewsChanged();
    }

    // 获取消息列表
    Observable<List<NewsBean>> listNews();

    // 注册消息监听器
    void register(OnNewsChangedListener listener);

    // 解绑消息监听器
    void unregister(OnNewsChangedListener listener);
}
