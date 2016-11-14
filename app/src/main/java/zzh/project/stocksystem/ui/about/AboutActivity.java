package zzh.project.stocksystem.ui.about;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.ui.base.BaseStackActivity;

public class AboutActivity extends BaseStackActivity {
    @BindView(R.id.tv_About_AppName)
    TextView mAppName;
    @BindView(R.id.tv_About_Version)
    TextView mVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_black);
        ab.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        try {
            PackageInfo pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mAppName.setText(getString(R.string.app_name));
            mVersion.setText(pkgInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onContextItemSelected(item);
    }
}
