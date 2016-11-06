package zzh.project.stocksystem.ui.stocktrace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;

public class TradeFragment extends Fragment {
    @BindView(R.id.vp_Trade_Page)
    ViewPager mPager;
    TabLayout mTabBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_trade, container, false);
        ButterKnife.bind(this, root);
        mTabBar = (TabLayout) getActivity().findViewById(R.id.tab);
        initView();
        return root;
    }

    private void initView() {
        mPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new TradeListFragment();
                } else {
                    return new HoldFragment();
                }
            }

            @Override
            public int getCount() {
                return mPageTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mPageTitles[position];
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mTabBar != null) {
            mTabBar.setVisibility(isVisibleToUser ? View.VISIBLE : View.GONE);
            mTabBar.setupWithViewPager(mPager);
        }
    }

    private String[] mPageTitles = new String[]{
            "流水",
            "持有"
    };
}
