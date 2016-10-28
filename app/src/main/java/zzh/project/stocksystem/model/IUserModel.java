package zzh.project.stocksystem.model;

import zzh.project.stocksystem.bean.UserBean;
import zzh.project.stocksystem.domain.AccessToken;

public interface IUserModel {
    interface Callback<T1, T2> {
        void onSuccess(T1 t1);

        void onError(T2 t2);
    }

    // 获取历史用户名
    String getHistoryUser();

    // 设置历史用户名
    void setHistoryUser(String history);

    // 登陆
    void login(String username, String password, Callback<AccessToken, String> callback);

    // 登陆
    void register(UserBean userBean, Callback<Void, String> callback);

    // 检测access_token
    boolean checkAccessToken();

    // 保存access_token
    void saveAccessToken(AccessToken accessToken);
}
