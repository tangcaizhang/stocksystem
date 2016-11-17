package zzh.project.stocksystem.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class NewsBean {
    @DatabaseField(id = true)
    public Long _id;
    @DatabaseField
    public String message;
}
