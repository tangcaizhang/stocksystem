package zzh.project.stocksystem.model;

public interface SettingsModel {
    /**
     * 启用推送
     */
    void enablePush();

    /**
     * 禁用推送
     */
    void disablePush();

    /**
     * 当前是否启用推送
     */
    boolean isEnablePush();
}
