package zzh.project.stocksystem.model.impl.juhe;

import java.util.List;

public class JuheListRespUSA {

    /**
     * reason : SUCCESSED!
     * error_code : 0
     * result : {"totalCount":"8968","page":"1","num":"20","data":[{"cname":"苹果公司","category":"计算机","symbol":"AAPL","price":"126.52","diff":"1.09","chg":"0.87","preclose":"125.42","open":"126.90","high":"126.94","low":"125.99","amplitude":"0.76%","volume":"30215693","mktcap":"741843599071","market":"NASDAQ"}]}
     */

    public String reason;
    public int error_code;
    /**
     * totalCount : 8968
     * page : 1
     * num : 20
     * data : [{"cname":"苹果公司","category":"计算机","symbol":"AAPL","price":"126.52","diff":"1.09","chg":"0.87","preclose":"125.42","open":"126.90","high":"126.94","low":"125.99","amplitude":"0.76%","volume":"30215693","mktcap":"741843599071","market":"NASDAQ"}]
     */

    public Result result;

    public static class Result {
        public String totalCount;
        public String page;
        public String num;
        /**
         * cname : 苹果公司
         * category : 计算机
         * symbol : AAPL
         * price : 126.52
         * diff : 1.09
         * chg : 0.87
         * preclose : 125.42
         * open : 126.90
         * high : 126.94
         * low : 125.99
         * amplitude : 0.76%
         * volume : 30215693
         * mktcap : 741843599071
         * market : NASDAQ
         */

        public List<Data> data;

        public static class Data {
            public String cname;
            public String category;
            public String symbol;
            public String price;
            public String diff;
            public String chg;
            public String preclose;
            public String open;
            public String high;
            public String low;
            public String amplitude;
            public String volume;
            public String mktcap;
            public String market;
        }
    }
}
