package zzh.project.stocksystem.bean;

import java.io.Serializable;

public class TradeBean implements Serializable {
    public String stockCode;
    public String stockName;
    public String uPrice;
    public int amount;
    public int status; // 0 处理中，1 处理完毕
    public String type; // "buy", "sell"
    public String date;
}
