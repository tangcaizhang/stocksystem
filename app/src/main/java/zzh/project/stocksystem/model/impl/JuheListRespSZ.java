package zzh.project.stocksystem.model.impl;

import java.util.List;

class JuheListRespSZ {

    /**
     * error_code : 0
     * reason : SUCCESSED!
     * result : {"totalCount":"1765","page":"1","num":"20","data":[{"symbol":"sz300001","name":"特锐德","trade":"20.450","pricechange":"0.060","changepercent":"0.294","buy":"20.440","sell":"20.450","settlement":"20.390","open":"21.010","high":"21.040","low":"19.610","volume":71773,"amount":146356152,"ticktime":"10:41:25"}]}
     */

    public int error_code;
    public String reason;
    /**
     * totalCount : 1765
     * page : 1
     * num : 20
     * data : [{"symbol":"sz300001","name":"特锐德","trade":"20.450","pricechange":"0.060","changepercent":"0.294","buy":"20.440","sell":"20.450","settlement":"20.390","open":"21.010","high":"21.040","low":"19.610","volume":71773,"amount":146356152,"ticktime":"10:41:25"}]
     */

    public Result result;

    public static class Result {
        public String totalCount;
        public String page;
        public String num;
        /**
         * symbol : sz300001
         * name : 特锐德
         * trade : 20.450
         * pricechange : 0.060
         * changepercent : 0.294
         * buy : 20.440
         * sell : 20.450
         * settlement : 20.390
         * open : 21.010
         * high : 21.040
         * low : 19.610
         * volume : 71773
         * amount : 146356152
         * ticktime : 10:41:25
         */

        public List<Data> data;

        public static class Data {
            public String symbol;
            public String name;
            public String trade;
            public String pricechange;
            public String changepercent;
            public String buy;
            public String sell;
            public String settlement;
            public String open;
            public String high;
            public String low;
            public int volume;
            public int amount;
            public String ticktime;
        }
    }
}
