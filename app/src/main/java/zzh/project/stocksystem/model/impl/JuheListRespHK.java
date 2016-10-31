package zzh.project.stocksystem.model.impl;


import java.util.List;

class JuheListRespHK {

    /**
     * error_code : 0
     * reason : SUCCESSED!
     * result : {"totalCount":"273","page":"2","num":"20","data":[{"symbol":"00066","name":"港铁公司","engname":"MTR CORPORATION","lasttrade":"35.900","prevclose":"36.100","open":"35.900","high":"35.900","low":"35.900","volume":"178000","amount":"6389923","ticktime":"2015-07-02 09:20:00","buy":"35.800","sell":"35.900","high_52week":"40.000","low_52week":"29.250","stocks_sum":"5839612547","pricechange":"-0.200","changepercent":"-0.5540166"}]}
     */

    public int error_code;
    public String reason;
    /**
     * totalCount : 273
     * page : 2
     * num : 20
     * data : [{"symbol":"00066","name":"港铁公司","engname":"MTR CORPORATION","lasttrade":"35.900","prevclose":"36.100","open":"35.900","high":"35.900","low":"35.900","volume":"178000","amount":"6389923","ticktime":"2015-07-02 09:20:00","buy":"35.800","sell":"35.900","high_52week":"40.000","low_52week":"29.250","stocks_sum":"5839612547","pricechange":"-0.200","changepercent":"-0.5540166"}]
     */

    public Result result;

    public static class Result {
        public String totalCount;
        public String page;
        public String num;
        /**
         * symbol : 00066
         * name : 港铁公司
         * engname : MTR CORPORATION
         * lasttrade : 35.900
         * prevclose : 36.100
         * open : 35.900
         * high : 35.900
         * low : 35.900
         * volume : 178000
         * amount : 6389923
         * ticktime : 2015-07-02 09:20:00
         * buy : 35.800
         * sell : 35.900
         * high_52week : 40.000
         * low_52week : 29.250
         * stocks_sum : 5839612547
         * pricechange : -0.200
         * changepercent : -0.5540166
         */

        public List<Data> data;

        public static class Data {
            public String symbol;
            public String name;
            public String engname;
            public String lasttrade;
            public String prevclose;
            public String open;
            public String high;
            public String low;
            public String volume;
            public String amount;
            public String ticktime;
            public String buy;
            public String sell;
            public String high_52week;
            public String low_52week;
            public String stocks_sum;
            public String pricechange;
            public String changepercent;
        }
    }
}
