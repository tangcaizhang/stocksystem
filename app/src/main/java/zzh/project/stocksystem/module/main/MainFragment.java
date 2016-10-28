package zzh.project.stocksystem.module.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.module.register.RegisterFragment;

public class MainFragment extends Fragment {
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
        Log.d("MainFragment", "onCreateView");
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, root);
        mTabBar = (TabLayout) getActivity().findViewById(R.id.tab);
        initView();
        return root;
    }

    private void initView() {
        mPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new RegisterFragment();
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "泸深股";
                    case 1:
                        return "泸港股";
                    case 2:
                        return "香港股";
                    case 3:
                        return "美国股";
                }
                return "泸深股";
            }
        });
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
