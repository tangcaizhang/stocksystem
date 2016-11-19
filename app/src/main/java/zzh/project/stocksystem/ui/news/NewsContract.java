package zzh.project.stocksystem.ui.news;

import java.util.List;

import zzh.project.stocksystem.bean.NewsBean;
import zzh.project.stocksystem.ui.base.IPresenter;
import zzh.project.stocksystem.ui.base.IView;

interface NewsContract {
    interface View extends IView {
        // 显示消息记录
        void showNews(List<NewsBean> news);

        // 显示loading图标
        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends IPresenter {
        // 加载消息列表
        void loadNews(boolean manual);
    }
}
