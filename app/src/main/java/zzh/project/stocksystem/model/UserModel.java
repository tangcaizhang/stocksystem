package zzh.project.stocksystem.model;

import java.util.List;

import zzh.project.stocksystem.bean.AccessToken;
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.bean.HoldStockBean;
import zzh.project.stocksystem.bean.TradeBean;
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
    void register(UserBean user, Callback2<Void, String> callback);

    // 获取个人信息
    void getInfo(Callback2<UserBean, String> callback);

    // 检测access_token
    void checkAccessToken(Callback2<Void, Void> callback);

    // 保存access_token
    void saveAccessToken(AccessToken accessToken);

    // 关注
    void favor(String gid, Callback2<Void, String> callback);

    // 取消关注
    void unFavor(String gid, Callback2<Void, String> callback);

    // 获取关注列表
    void listFavor(Callback2<List<String>, String> callback);

    // 获取绑定卡片信息
    void getAccount(Callback2<AccountBean, String> callback);

    // 绑定卡片
    void bindAccount(AccountBean accountBean, Callback2<Void, String> callback);

    // 充值
    void recharge(String cardNum, String password, float money, Callback2<Void, String> callback);

    // 获取交易记录
    void listTrade(Callback2<List<TradeBean>, String> callback);

    // 购买
    void buy(String gid, String name, float uPrice, int amount, Callback2<Void, String> callback);

    // 卖出
    void sell(String gid, String name, float uPrice, int amount, Callback2<Void, String> callback);

    // 获取持有股票
    void listHoldStock(Callback2<List<HoldStockBean>, String> callback);
}
