package zzh.project.stocksystem.model;

import java.util.List;

import rx.Observable;
import zzh.project.stocksystem.bean.AccessToken;
import zzh.project.stocksystem.bean.AccountBean;
import zzh.project.stocksystem.bean.HoldStockBean;
import zzh.project.stocksystem.bean.TradeBean;
import zzh.project.stocksystem.bean.UserBean;

public interface UserManager {

    // 获取历史用户名
    String getHistoryUser();

    // 设置历史用户名
    void setHistoryUser(String history);

    // 登陆
    Observable<AccessToken> login(String username, String password);

    // 登出
    void logout();

    // 注冊
    void register(UserBean user) throws Exception;

    // 获取个人信息
    Observable<UserBean> getInfo();

    // 检测access_token
    boolean checkAccessToken();

    // 保存access_token
    void saveAccessToken(AccessToken accessToken);

    // 关注
    void favor(String gid) throws Exception;

    // 取消关注
    void unFavor(String gid) throws Exception;

    // 获取关注列表
    Observable<List<String>> listFavor();

    // 获取绑定卡片信息
    Observable<AccountBean> getAccount();

    // 绑定卡片
    void bindAccount(AccountBean accountBean) throws Exception;

    // 充值
    void recharge(String cardNum, String password, float money) throws Exception;

    // 获取交易记录
    Observable<List<TradeBean>> listTrade();

    // 购买
    void buy(String gid, String name, float uPrice, int amount) throws Exception;

    // 卖出
    void sell(String gid, String name, float uPrice, int amount) throws Exception;

    // 获取持有股票
    Observable<List<HoldStockBean>> listHoldStock();
}
