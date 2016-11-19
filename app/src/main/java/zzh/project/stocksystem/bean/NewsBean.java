package zzh.project.stocksystem.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class NewsBean implements Serializable {
    @DatabaseField(generatedId = true)
    public Long _id;
    @DatabaseField
    public String title;
    @DatabaseField
    public String message;
    @DatabaseField
    public Date date;

    public NewsBean() {

    }

    public NewsBean(String title, String message) {
        this.title = title;
        this.message = message;
        this.date = new Date();
    }
}
