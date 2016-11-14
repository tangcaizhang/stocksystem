package zzh.project.stocksystem.ui.favor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.stock.StockListAdapter;
import zzh.project.stocksystem.ui.stockdetail.StockDetailActivity;
import zzh.project.stocksystem.widget.ScrollChildSwipeRefreshLayout;

public class FavorFragment extends BaseFragment<FavorContract.Presenter> implements FavorContract.View {
    @BindView(R.id.rv_List)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_Refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    private List<StockBean> mData = new ArrayList<>(0);
    private StockListAdapter mAdapter;

    @Override
    public FavorContract.Presenter createPresenter() {
        return new FavorPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_list, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        mAdapter = new StockListAdapter(getContext(), mData);
        mAdapter.setOnItemClickedListener(new StockListAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                toStockDetailActivity(position);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setScrollUpChild(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadFavorList(true);
            }
        });
    }

    private void toStockDetailActivity(int position) {
        if (mData != null) {
            StockBean bean = mData.get(position);
            Intent intent = new Intent(getContext(), StockDetailActivity.class);
            intent.putExtra("gid", bean.gid);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 数据改变过
        if (requestCode == 0 && resultCode == 1) {
            Log.d(TAG, "onActivityResult requestCode=" + requestCode + ", resultCode=" + resultCode);
            mPresenter.loadFavorList(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void appendFavorStock(StockBean stockBean) {
        mData.add(stockBean);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearAllFavorStock() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.setRefreshing(active);
    }
}
