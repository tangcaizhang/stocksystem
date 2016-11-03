package zzh.project.stocksystem;

public final class ApiUrl {

    public static final String BASE_URL = "http://192.168.31.184:8080/stocksystem/app";

    public static final String SERVER_LOGIN = BASE_URL + "/login";

    public static final String SERVER_REGISTER = BASE_URL + "/register";

    public static final String SERVER_GET_INFO = BASE_URL + "/getinfo";

    public static final String SERVER_FAVOR = BASE_URL + "/favor";

    public static final String SERVER_UNFAVOR = BASE_URL + "/unfavor";

    public static final String SERVER_LIST_FAVOR = BASE_URL + "/listfavor";

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
