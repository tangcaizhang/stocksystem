package zzh.project.stocksystem.ui.stocktrace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.HoldStockBean;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.win.TradePopWin;
import zzh.project.stocksystem.widget.ScrollChildSwipeRefreshLayout;

public class HoldFragment extends BaseFragment<HoldContract.Presenter> implements HoldContract.View {
    @BindView(R.id.rv_List)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_Refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    private HoldStockAdapter mAdapter;
    private List<HoldStockBean> mData = new ArrayList<>(0);
    private TradePopWin mSellPopWin;
    private HoldStockBean mSelectedStock;

    @Override
    public HoldContract.Presenter createPresenter() {
        return new HoldPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_list, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDetach() {
        mSelectedStock = null;
        super.onDetach();
    }

    private void initView() {
        mAdapter = new HoldStockAdapter(getContext(), mData);
        mAdapter.setOnItemClickedListener(new HoldStockAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                mSelectedStock = mData.get(position);
                if (!mSellPopWin.isShowing()) {
                    mSellPopWin.setMax(mSelectedStock.total);
                    mSellPopWin.showAtLocation(getActivity().findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 0.5f;
                    getActivity().getWindow().setAttributes(lp);
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setScrollUpChild(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadHoldStockList(true);
            }
        });
        mSellPopWin = new TradePopWin(getContext());
        mSellPopWin.setTitle("抛出股票");
        mSellPopWin.setLabel("抛出数目：");
        mSellPopWin.setDone("抛出");
        mSellPopWin.setOnDoneListener(new TradePopWin.OnDoneListener() {
            @Override
            public void done(int num) {
                mPresenter.sell(num);
            }
        });
        mSellPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void appendHoldStock(HoldStockBean stockBean) {
        mData.add(stockBean);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearAllHoldStock() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.setRefreshing(active);
    }

    @Override
    public HoldStockBean getSelected() {
        return mSelectedStock;
    }

    @Override
    public void hideSellPop() {
        mSellPopWin.dismiss();
    }
}
