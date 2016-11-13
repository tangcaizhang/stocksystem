package zzh.project.stocksystem.model;

import java.util.List;

import rx.Observable;
import zzh.project.stocksystem.bean.StockBean;
import zzh.project.stocksystem.bean.StockDetailBean;

public interface StockModel2 {
    /**
     * 查询股票详情
     */
    Observable<StockDetailBean> getDetail(String gid);

    /**
     * 香港股票列表
     */
    Observable<List<StockBean>> findAllHK(int page);

    /**
     * 美国股票列表
     */
    Observable<List<StockBean>> findAllUS(int page);

    /**
     * 深圳股票列表
     */
    Observable<List<StockBean>> findAllSZ(int page);

    /**
     * 泸股列表
     */
    Observable<List<StockBean>> findAllSH(int page);
}
