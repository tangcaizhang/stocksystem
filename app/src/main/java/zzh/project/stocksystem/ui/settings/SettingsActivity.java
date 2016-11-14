package zzh.project.stocksystem.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zzh.project.stocksystem.R;
import zzh.project.stocksystem.ui.base.BaseStackActivity;

public class SettingsActivity extends BaseStackActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_black);
        ab.setDisplayHomeAsUpEnabled(true);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentFrame, new SettingsFragment());
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
