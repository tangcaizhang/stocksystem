package zzh.project.stocksystem.ui.stock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockListType;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.stockdetail.StockDetailActivity;
import zzh.project.stocksystem.widget.ScrollChildSwipeRefreshLayout;

public class StockListFragment extends BaseFragment<StockListContract.Presenter> implements StockListContract.View {

    @BindView(R.id.rl_StockList_Refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_StockList_List)
    RecyclerView mRecyclerView;
    LoadMoreWrapper mAdapter;
    LinearLayoutManager mLayoutManager;
    View mLoadMore;

    private StockListType mType;
    private List<StockBean> mData = new ArrayList<>(0);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = (StockListType) getArguments().getSerializable("type");
    }

    @Override
    public StockListContract.Presenter createPresenter() {
        return new StockListPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_stock_list, container, false);
        mLoadMore = buildLoadMoreView();
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumb;
        public TextView name;
        public TextView price;
        public TextView code;
        public TextView increase;
        public TextView increPer;

        public ViewHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.Stock_Thumb);
            name = (TextView) itemView.findViewById(R.id.Stock_Name);
            price = (TextView) itemView.findViewById(R.id.Stock_Price);
            code = (TextView) itemView.findViewById(R.id.Stock_Code);
            increase = (TextView) itemView.findViewById(R.id.Stock_Increase);
            increPer = (TextView) itemView.findViewById(R.id.Stock_IncrePer);
        }
    }

    private void initView() {
        StockListAdapter innerAdapter = new StockListAdapter(getContext(), mData);
        innerAdapter.setOnItemClickedListener(new StockListAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                toStockDetailActivity(position);
            }
        });
        mAdapter = new LoadMoreWrapper<ViewHolder>(innerAdapter);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(mLoadMore);
        mRefreshLayout.setScrollUpChild(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadStocks(true);
            }
        });
        mAdapter.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mData.size() > 0) {
                    mPresenter.loadMore();
                }
            }
        });
    }

    private void toStockDetailActivity(int position) {
        if (mData != null) {
            StockBean bean = mData.get(position);
            Intent intent = new Intent(getContext(), StockDetailActivity.class);
            intent.putExtra("gid", bean.gid);
            startActivity(intent);
        }
    }

    @Override
    public void showStocks(List<StockBean> stocks) {
        mData.clear();
        mData.addAll(stocks);
        mAdapter.showLoadMore();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void appendStocks(List<StockBean> stocks) {
        if (stocks == null || stocks.size() == 0) {
            mAdapter.hideLoadMore();
        } else {
            mData.addAll(stocks);
        }
        if (!mRecyclerView.isComputingLayout() && mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            mAdapter.notifyDataSetChanged();
        } else {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    mRecyclerView.removeOnScrollListener(this);
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.setRefreshing(active);
    }

    @Override
    public StockListType getType() {
        return mType;
    }

    private View buildLoadMoreView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.v_load_more, null, false);
    }
}
