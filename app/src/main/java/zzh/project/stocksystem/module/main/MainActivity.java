package zzh.project.stocksystem.module.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.module.favor.FavorFragment;
import zzh.project.stocksystem.module.info.InfoFragment;
import zzh.project.stocksystem.module.news.NewsFragment;
import zzh.project.stocksystem.module.stocktrace.TradeFragment;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.vp_Main_Content)
    ViewPager mViewPager;
    @BindView(R.id.rg_NavBar)
    RadioGroup mNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mViewPager.setAdapter(mAdapter);
        mNavBar.setOnCheckedChangeListener(this);
    }

    private FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new FavorFragment();
                case 2:
                    return new TradeFragment();
                case 3:
                    return new NewsFragment();
                case 4:
                    return new InfoFragment();
            }
            return new MainFragment();
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int pageIndex = 0;
        switch (checkedId) {
            case R.id.rb_Nav_Index:
                pageIndex = 0;
                break;
            case R.id.rb_Nav_Choose:
                pageIndex = 1;
                break;
            case R.id.rb_Nav_Trade:
                pageIndex = 2;
                break;
            case R.id.rb_Nav_News:
                pageIndex = 3;
                break;
            case R.id.rb_Nav_My:
                pageIndex = 4;
                break;
        }
        mViewPager.setCurrentItem(pageIndex);
    }
}
