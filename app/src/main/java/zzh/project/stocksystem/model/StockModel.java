package zzh.project.stocksystem.model;

import java.util.List;

import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockDetailBean;

public interface StockModel {

    /**
     * 查询股票详情
     */
    void getDetail(String gid, Callback2<StockDetailBean, String> callback);

    /**
     * 香港股票列表
     */
    void findAllHK(int page, Callback2<List<StockBean>, String> callback);

    /**
     * 美国股票列表
     */
    void findAllUS(int page, Callback2<List<StockBean>, String> callback);

    /**
     * 深圳股票列表
     */
    void findAllSZ(int page, Callback2<List<StockBean>, String> callback);

    /**
     * 泸股列表
     */
    void findAllSH(int page, Callback2<List<StockBean>, String> callback);
}
