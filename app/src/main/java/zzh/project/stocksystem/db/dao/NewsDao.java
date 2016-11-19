package zzh.project.stocksystem.db.dao;

import zzh.project.stocksystem.bean.NewsBean;

public class NewsDao extends BaseDao<NewsBean, Long> {
    private static NewsDao sInstance;

    public static NewsDao getInstance() {
        if (sInstance == null) {
            synchronized (NewsDao.class) {
                if (sInstance == null) {
                    sInstance = new NewsDao();
                }
            }
        }
        return sInstance;
    }
}
