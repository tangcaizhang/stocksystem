package zzh.project.stocksystem.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.NewsBean;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.widget.ScrollChildSwipeRefreshLayout;

public class NewsFragment extends BaseFragment<NewsContract.Presenter> implements NewsContract.View {
    @BindView(R.id.rl_Refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_List)
    RecyclerView mRecyclerView;
    NewsListAdapter mAdapter;
    private List<NewsBean> mData = new ArrayList<>(0);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_list, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        mAdapter = new NewsListAdapter(getContext(), mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setScrollUpChild(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadNews(true);
            }
        });
    }

    @Override
    public NewsContract.Presenter createPresenter() {
        return new NewsPresenter(this);
    }

    @Override
    public void showNews(List<NewsBean> news) {
        mData.clear();
        mData.addAll(news);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.setRefreshing(active);
    }
}
