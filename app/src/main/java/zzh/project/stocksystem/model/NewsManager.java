package zzh.project.stocksystem.model;

import java.util.List;

import rx.Observable;
import zzh.project.stocksystem.bean.NewsBean;

public interface NewsManager {
    // 获取消息列表
    Observable<List<NewsBean>> listNews();
}
