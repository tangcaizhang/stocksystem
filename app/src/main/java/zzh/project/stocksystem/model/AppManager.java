package zzh.project.stocksystem.model;

import zzh.project.stocksystem.bean.VersionBean;

public interface AppManager {
    /**
     * 版本检测，返回最后版本信息
     */
    VersionBean checkVersion();

    /**
     * 更新
     */
    void update();
}
