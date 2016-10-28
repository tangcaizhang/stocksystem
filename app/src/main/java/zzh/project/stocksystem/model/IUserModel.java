package zzh.project.stocksystem.model;

public interface IUserModel {
    interface LoginCallback {
        void onLoginSuccess();

        void onLoginError(String errMsg);
    }

    // 获取历史用户名
    String getHistoryUser();

    // 设置历史用户名
    void setHistoryUser(String history);

    // 登陆
    void login(String username, String password, LoginCallback callback);

    // 检测access_token
    boolean checkAccessToken();

    // 获取access_token
    String getAccessToken();
}
