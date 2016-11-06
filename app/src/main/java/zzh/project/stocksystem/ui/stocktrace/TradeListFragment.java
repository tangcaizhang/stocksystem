package zzh.project.stocksystem.ui.stocktrace;

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
import zzh.project.stocksystem.bean.TradeBean;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.widget.ScrollChildSwipeRefreshLayout;

public class TradeListFragment extends BaseFragment implements TradeListContract.View {
    @BindView(R.id.rv_List)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_Refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    TradeListPresenter mPresenter;
    private List<TradeBean> mData = new ArrayList<>(0);
    private TradeListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TradeListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_list, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        mAdapter = new TradeListAdapter(getContext(), mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setScrollUpChild(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTradeList();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDetach() {
        mPresenter.destroy();
        super.onDetach();
    }

    @Override
    public void clearAllTrade() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showTrade(List<TradeBean> trades) {
        mData.clear();
        mData.addAll(trades);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.setRefreshing(active);
    }
}
