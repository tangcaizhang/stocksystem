package zzh.project.stocksystem.model;

import zzh.project.stocksystem.bean.AccessToken;
import zzh.project.stocksystem.bean.UserBean;

public interface UserModel {

    // 获取历史用户名
    String getHistoryUser();

    // 设置历史用户名
    void setHistoryUser(String history);

    // 登陆
    void login(String username, String password, Callback2<AccessToken, String> callback);

    // 登出
    void logout();

    // 注冊
    void register(UserBean userBean, Callback2<Void, String> callback);

    // 检测access_token
    boolean checkAccessToken();

    // 保存access_token
    void saveAccessToken(AccessToken accessToken);
}
