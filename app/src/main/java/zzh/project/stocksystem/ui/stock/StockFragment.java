package zzh.project.stocksystem.ui.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.StockListType;
import zzh.project.stocksystem.ui.SearchFragment;

public class StockFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    TabLayout mTabBar;

    @BindView(R.id.vp_Main_Page)
    ViewPager mPager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_stock, container, false);
        ButterKnife.bind(this, root);
        mTabBar = (TabLayout) getActivity().findViewById(R.id.tab);
        initView();
        return root;
    }

    class PageTitle {
        public final StockListType type;
        public final String name;

        public PageTitle(StockListType type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    private PageTitle[] mPageTitles = new PageTitle[]
            {
                    new PageTitle(StockListType.SH, "泸股"),
                    new PageTitle(StockListType.SZ, "深股"),
                    new PageTitle(StockListType.HK, "港股"),
                    new PageTitle(StockListType.USA, "美股")
            };

    private void initView() {
        mPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                StockListType type = mPageTitles[position].type;
                StockListFragment stockFragment = new StockListFragment();
                Bundle args = new Bundle();
                args.putSerializable("type", type);
                stockFragment.setArguments(args);
                return stockFragment;
            }

            @Override
            public int getCount() {
                return mPageTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mPageTitles[position].name;
            }

        });
        mPager.setOffscreenPageLimit(mPageTitles.length);
        if (getUserVisibleHint()) {
            mTabBar.setVisibility(View.VISIBLE);
        }
        mTabBar.setupWithViewPager(mPager);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mTabBar != null) {
            mTabBar.setVisibility(isVisibleToUser ? View.VISIBLE : View.GONE);
            mTabBar.setupWithViewPager(mPager);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Main_Menu_Search:
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.appBar);
                if (fragment != null && fragment instanceof SearchFragment) {
                    return true;
                }
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.appBar,
                        new SearchFragment()).addToBackStack("search_view").commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
