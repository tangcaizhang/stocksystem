package zzh.project.stocksystem.bean;


import java.io.Serializable;

public class StockBean implements Serializable{
    public String gid; // 股票编号
    public String name; // 股票名称
    public String nowPri; // 当前价格
    public String thumbUrl; // 略缩图
    public String increase; // 涨跌额
    public String increPer; // 涨跌幅
}
