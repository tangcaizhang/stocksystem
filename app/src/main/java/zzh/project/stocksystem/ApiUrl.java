package zzh.project.stocksystem;

public final class ApiUrl {

    private static final String BASE_URL = "http://192.168.31.184:8080/stocksystem/app";

    public static final String SERVER_LOGIN = BASE_URL + "/login";

    public static final String SERVER_REGISTER = BASE_URL + "/register";

    public static final String SERVER_GET_INFO = BASE_URL + "/getInfo";

    public static final String SERVER_FAVOR = BASE_URL + "/favor";

    public static final String SERVER_UNFAVOR = BASE_URL + "/unFavor";

    public static final String SERVER_LIST_FAVOR = BASE_URL + "/listFavor";

    public static final String SERVER_RECHARGE = BASE_URL + "/recharge";

    public static final String SERVER_BIND_ACCOUNT = BASE_URL + "/bind";

    public static final String SERVER_GET_ACCOUNT = BASE_URL + "/getAccount";

    public static final String SERVER_LIST_TRADE = BASE_URL + "/listTrade";

    public static final String SERVER_BUY = BASE_URL + "/buy";

    public static final String SERVER_SELL = BASE_URL + "/sell";

    public static final String SERVER_LIST_STOCK = BASE_URL + "/listStock";

    public static final String SERVER_CHECK = BASE_URL + "/check";

    public static final String STOCK_API_BASE_URL = "http://web.juhe.cn:8080";

    public static final String STOCK_DETAIL_HS = STOCK_API_BASE_URL + "/finance/stock/hs";

    public static final String STOCK_DETAIL_HK = STOCK_API_BASE_URL + "/finance/stock/hk";

    public static final String STOCK_DETAIL_USA = STOCK_API_BASE_URL + "/finance/stock/usa";

    public static final String STOCK_LIST_HK = STOCK_API_BASE_URL + "/finance/stock/hkall";

    public static final String STOCK_LIST_US = STOCK_API_BASE_URL + "/finance/stock/usaall";

    public static final String STOCK_LIST_SZ = STOCK_API_BASE_URL + "/finance/stock/szall";

    public static final String STOCK_LIST_SH = STOCK_API_BASE_URL + "/finance/stock/shall";

}
