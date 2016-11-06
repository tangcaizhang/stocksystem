package zzh.project.stocksystem.ui.stockdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.ui.base.BaseFragment;
import zzh.project.stocksystem.ui.win.TradePopWin;

public class StockDetailFragment extends BaseFragment implements StockDetailContract.View {

    private StockDetailContract.Presenter mPresenter;

    @BindView(R.id.Stock_Result_View)
    View mResultView;
    @BindView(R.id.Stock_Not_Found_View)
    View mNotFoundView;
    @BindView(R.id.Stock_Chart)
    ViewPager mChart;
    @BindView(R.id.Stock_Chart_Indicator)
    CirclePageIndicator mIndicator;
    @BindView(R.id.Stock_Name)
    TextView mName;
    @BindView(R.id.Stock_Price)
    TextView mPrice;
    @BindView(R.id.Stock_Increase)
    TextView mIncrease;
    @BindView(R.id.Stock_IncrePer)
    TextView mIncrePer;
    @BindView(R.id.Stock_CompetitivePri)
    TextView mCompetitivePri;
    @BindView(R.id.Stock_ReservePri)
    TextView mReservePri;
    @BindView(R.id.Stock_YestodEndPri)
    TextView mYestodEndPri;
    @BindView(R.id.Stock_TodayStartPri)
    TextView mTodayStartPri;
    @BindView(R.id.Stock_TodayMax)
    TextView mTodayMax;
    @BindView(R.id.Stock_TodayMin)
    TextView mTodayMin;
    @BindView(R.id.Stock_TraNumber)
    TextView mTraNumber;
    @BindView(R.id.Stock_TraAmount)
    TextView mTraAmount;
    FloatingActionButton mActionButton;

    TradePopWin mTradePopWin;

    private String mGid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGid = getArguments().getString("gid");
        mPresenter = new StockDetailPresenter(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_stock_detail, container, false);
        ButterKnife.bind(this, root);
        initView();
        mPresenter.start();
        return root;
    }

    @Override
    public void onDetach() {
        mPresenter.destroy();
        super.onDetach();
    }

    private void initView() {
        mActionButton = (FloatingActionButton) getActivity().findViewById(R.id.action_button);
        mActionButton.setImageResource(R.drawable.ic_shopping_basket);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTradePopWin.isShowing()) {
                    mTradePopWin.showAtLocation(getActivity().findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 0.5f;
                    getActivity().getWindow().setAttributes(lp);
                }
            }
        });
        mTradePopWin = new TradePopWin(getContext());
        mTradePopWin.setTitle("购买股票");
        mTradePopWin.setLabel("购买数目：");
        mTradePopWin.setDone("购买");
        mTradePopWin.setOnDoneListener(new TradePopWin.OnDoneListener() {
            @Override
            public void done(int num) {
                mPresenter.buy(num);
            }
        });
        mTradePopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void showStockDetail(final StockDetailBean detail) {
        if (detail != null) {
            mFavor.setVisible(true);
            mActionButton.setVisibility(View.VISIBLE);
            mNotFoundView.setVisibility(View.GONE);
            mResultView.setVisibility(View.VISIBLE);
            mChart.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    ImageFragment fragment = new ImageFragment();
                    String url = null;
                    switch (position) {
                        case 0:
                            url = detail.minUrl;
                            break;
                        case 1:
                            url = detail.dayUrl;
                            break;
                        case 2:
                            url = detail.weekUrl;
                            break;
                        case 3:
                            url = detail.monthUrl;
                            break;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    fragment.setArguments(bundle);
                    return fragment;
                }

                @Override
                public int getCount() {
                    return 4;
                }
            });
            mIndicator.setViewPager(mChart);
            mName.setText(detail.name);
            mPrice.setText(String.format("最近价格：%s", detail.nowPri));
            mIncrease.setText(String.format("涨跌　额：%s", detail.increase));
            mIncrePer.setText(String.format("涨跌　幅：%s", detail.increPer));
            mCompetitivePri.setText(String.format("买　　入：%s", detail.competitivePri == null ? "unknown" : detail.competitivePri));
            mReservePri.setText(String.format("卖　　出：%s", detail.reservePri == null ? "unknown" : detail.reservePri));
            mYestodEndPri.setText(String.format("昨　　收：%s", detail.yestodEndPri));
            mTodayStartPri.setText(String.format("今　　开：%s", detail.todayStartPri));
            mTodayMax.setText(String.format("最　　高：%s", detail.todayMax));
            mTodayMin.setText(String.format("最　　低：%s", detail.todayMin));
            mTraNumber.setText(String.format("成交数量：%s", detail.traNumber));
            mTraAmount.setText(String.format("成交金额：%s", detail.traAmount));
        }
    }

    @Override
    public void showNotFound() {
        mFavor.setVisible(false);
        mActionButton.setVisibility(View.INVISIBLE);
        mResultView.setVisibility(View.GONE);
        mNotFoundView.setVisibility(View.VISIBLE);
    }

    @Override
    public String getGid() {
        return mGid;
    }

    @Override
    public void showUnFavor() {
        if (mFavor != null) {
            mFavor.setTitle("关注");
            mFavor.setIcon(R.drawable.ic_favor_n);
        }
    }

    @Override
    public void showAlreadyFavor() {
        if (mFavor != null) {
            mFavor.setTitle("取消关注");
            mFavor.setIcon(R.drawable.ic_favor_p);
        }
    }

    @Override
    public void hideBuyPop() {
        if (mTradePopWin != null) {
            mTradePopWin.dismiss();
        }
    }

    MenuItem mFavor;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favor, menu);
        mFavor = menu.findItem(R.id.Main_Menu_Favor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Main_Menu_Favor:
                if (item.getTitle().equals("取消关注")) {
                    mPresenter.cancelFavor();
                } else {
                    mPresenter.favor();
                }
                getActivity().setResult(1); // 操作过
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
