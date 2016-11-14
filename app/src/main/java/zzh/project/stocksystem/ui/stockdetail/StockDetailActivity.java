package zzh.project.stocksystem.ui.stockdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zzh.project.stocksystem.R;
import zzh.project.stocksystem.ui.base.BaseStackActivity;

public class StockDetailActivity extends BaseStackActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_stock_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_black);
        ab.setDisplayHomeAsUpEnabled(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        StockDetailFragment stockFragment = new StockDetailFragment();
        if (getIntent() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("gid", getIntent().getStringExtra("gid"));
            stockFragment.setArguments(bundle);
        }
        transaction.add(R.id.contentFrame, stockFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onContextItemSelected(item);
    }
}
