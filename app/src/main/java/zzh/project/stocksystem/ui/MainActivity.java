package zzh.project.stocksystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.ui.base.BaseStackActivity;
import zzh.project.stocksystem.ui.favor.FavorFragment;
import zzh.project.stocksystem.ui.info.InfoFragment;
import zzh.project.stocksystem.ui.news.NewsFragment;
import zzh.project.stocksystem.ui.stock.StockFragment;
import zzh.project.stocksystem.ui.stocktrace.TradeFragment;
import zzh.project.stocksystem.util.ToastUtil;

public class MainActivity extends BaseStackActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String ARGUMENT_PAGE_INDEX = "page_index";

    @BindView(R.id.vp_Main_Content)
    ViewPager mViewPager;
    @BindView(R.id.rg_NavBar)
    RadioGroup mNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initView(savedInstanceState);

        if (getIntent().hasExtra(ARGUMENT_PAGE_INDEX)) {
            mViewPager.setCurrentItem(getIntent().getIntExtra(ARGUMENT_PAGE_INDEX, 0));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (getIntent().hasExtra(ARGUMENT_PAGE_INDEX)) {
            mViewPager.setCurrentItem(getIntent().getIntExtra(ARGUMENT_PAGE_INDEX, 0));
        }
    }

    private void initView(Bundle savedInstanceState) {
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton child = (RadioButton) mNavBar.getChildAt(position);
                child.setChecked(true);
                switch (position) {
                    case 0:
                        setTitle(getResources().getString(R.string.app_name));
                        break;
                    case 1:
                        setTitle("关注");
                        break;
                    case 2:
                        setTitle("交易");
                        break;
                    case 3:
                        setTitle("消息");
                        break;
                    case 4:
                        setTitle("个人");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOffscreenPageLimit(5);
        mNavBar.setOnCheckedChangeListener(this);
    }

    private FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new StockFragment();
                case 1:
                    return new FavorFragment();
                case 2:
                    return new TradeFragment();
                case 3:
                    return new NewsFragment();
                case 4:
                    return new InfoFragment();
            }
            return new StockFragment();
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

    long mLastBackPressed = 0;

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (fragment != null && fragment instanceof SearchFragment) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        long curTime = System.currentTimeMillis();
        if ((curTime - mLastBackPressed) >= 2000) {
            ToastUtil.show("再按一次退出程序", Toast.LENGTH_SHORT);
            mLastBackPressed = curTime;
        } else {
            finish();
        }
    }
}
