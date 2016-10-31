package zzh.project.stocksystem.ui.stock;

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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockListType;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.widget.ScrollChildSwipeRefreshLayout;

public class StockListFragment extends BaseFragment implements StockListContract.View {

    @BindView(R.id.rl_StockList_Refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_StockList_List)
    RecyclerView mRecyclerView;
    LoadMoreWrapper mAdapter;
    LinearLayoutManager mLayoutManager;
    private StockListType mType;
    private StockListPresenter mPresenter;
    private List<StockBean> mDatas = new ArrayList<>(0);
    private View mLoadMore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = (StockListType) getArguments().getSerializable("type");
        mPresenter = new StockListPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_stock_list, container, false);
        ButterKnife.bind(this, root);
        mLoadMore = buildLoadMoreView();
        initView();
        mPresenter.start();
        return root;
    }

    @Override
    public void onDetach() {
        mPresenter.destroy();
        super.onDetach();
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
        mAdapter = new LoadMoreWrapper<ViewHolder>(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View root = LayoutInflater.from(getContext()).inflate(R.layout.adapter_stock_item, parent, false);
                return new ViewHolder(root);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                StockBean bean = mDatas.get(position);
                ImageLoader.getInstance().displayImage(bean.thumbUrl, holder.thumb);
                holder.name.setText(bean.name);
                holder.price.setText(String.format("最新价格：%s", bean.nowPri));
                holder.code.setText(String.format("股票代码：%s", bean.gid));
                holder.increase.setText(String.format("涨幅额：%s", bean.increase));
                holder.increPer.setText(String.format("涨跌幅：%s%%", bean.increPer));
            }

            @Override
            public int getItemCount() {
                return mDatas.size();
            }
        });
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(mLoadMore);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadStocks();
            }
        });
        mAdapter.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore();
            }
        });
    }

    @Override
    public void showStocks(List<StockBean> stocks) {
        mDatas = stocks;
        mAdapter.notifyDataSetChanged();
        int lastCompVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();
        if (lastCompVisibleItem != mLayoutManager.getItemCount()) {
            mAdapter.setLoadMoreView(mLoadMore);
        } else {
            mAdapter.setLoadMoreView(null);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void appendStocks(List<StockBean> stocks) {
        if (stocks == null || stocks.size() == 0) {
            mAdapter.setLoadMoreView(null);
        } else {
            mDatas.addAll(stocks);
        }
        if (!mRecyclerView.isComputingLayout() && mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
            mAdapter.notifyDataSetChanged();
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
