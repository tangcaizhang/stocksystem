package zzh.project.stocksystem.model.impl.juhe;

import java.util.List;

public class JuheListRespSH {

    /**
     * error_code : 0
     * reason : SUCCESSED!
     * result : {"totalCount":"1086","page":"1","num":"20","data":[{"symbol":"sh600004","name":"白云机场","trade":"11.590","pricechange":"-0.130","changepercent":"-1.109","buy":"11.590","sell":"11.600","settlement":"11.720","open":"11.670","high":"11.800","low":"11.570","volume":38781,"amount":45385925,"code":"600004","ticktime":"15:00:00"}]}
     */

    public int error_code;
    public String reason;
    /**
     * totalCount : 1086
     * page : 1
     * num : 20
     * data : [{"symbol":"sh600004","name":"白云机场","trade":"11.590","pricechange":"-0.130","changepercent":"-1.109","buy":"11.590","sell":"11.600","settlement":"11.720","open":"11.670","high":"11.800","low":"11.570","volume":38781,"amount":45385925,"code":"600004","ticktime":"15:00:00"}]
     */

    public Result result;

    public static class Result {
        public String totalCount;
        public String page;
        public String num;
        /**
         * symbol : sh600004
         * name : 白云机场
         * trade : 11.590
         * pricechange : -0.130
         * changepercent : -1.109
         * buy : 11.590
         * sell : 11.600
         * settlement : 11.720
         * open : 11.670
         * high : 11.800
         * low : 11.570
         * volume : 38781
         * amount : 45385925
         * code : 600004
         * ticktime : 15:00:00
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
            public String code;
            public String ticktime;
        }
    }
}
