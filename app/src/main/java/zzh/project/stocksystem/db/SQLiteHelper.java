package zzh.project.stocksystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import zzh.project.stocksystem.EnvConst;
import zzh.project.stocksystem.bean.NewsBean;

public class SQLiteHelper extends OrmLiteSqliteOpenHelper {
    private final String TAG = this.getClass().getSimpleName();
    private static final String DATABASE_NAME = "stocksystem.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NewsBean.class);
            if (EnvConst.DEBUG) {
                Log.d(TAG, "onCreate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, NewsBean.class, true);
            onCreate(database, connectionSource);
            if (EnvConst.DEBUG) {
                Log.d(TAG, "onUpgrade, version=" + newVersion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
