package zzh.project.stocksystem.model.impl.juhe;

import java.util.List;

public class JuheDetailRespUSA {

    /**
     * resultcode : 200
     * reason : SUCCESSED!
     * result : [{"data":{"gid":"aapl","name":"苹果","lastestpri":"437.87","openpri":"429.70","formpri":"431.72","maxpri":"439.01","minpri":"425.14","uppic":"6.15","limit":"1.42","traAmount":"16884191","avgTraNumber":"16910781","markValue":"411185326460","max52":"705.07","min52":"419.00","EPS":"44.16","priearn":"9.92","beta":"1.10","divident":"10.60","ROR":"10.24","capital":"939058000","afterpic":"437.29","afterlimit":"-0.13","afteruppic":"-0.58","aftertime":"Mar 11 7:59PM EDT","ustime":"Mar 11 4:00PM EDT","chtime":"2013-03-12 07:58:04"},"gopicture":{"minurl":"http://image.sinajs.cn/newchartv5/usstock/min/aapl.gif","min_weekpic":"http://image.sinajs.cn/newchartv5/usstock/min_week/aapl.gif","dayurl":"http://image.sinajs.cn/newchartv5/usstock/daily/aapl.gif","weekurl":"http://image.sinajs.cn/newchartv5/usstock/weekly/aapl.gif","monthurl":"http://image.sinajs.cn/newchartv5/usstock/monthly/aapl.gif"}}]
     */

    public String resultcode;
    public String reason;
    /**
     * data : {"gid":"aapl","name":"苹果","lastestpri":"437.87","openpri":"429.70","formpri":"431.72","maxpri":"439.01","minpri":"425.14","uppic":"6.15","limit":"1.42","traAmount":"16884191","avgTraNumber":"16910781","markValue":"411185326460","max52":"705.07","min52":"419.00","EPS":"44.16","priearn":"9.92","beta":"1.10","divident":"10.60","ROR":"10.24","capital":"939058000","afterpic":"437.29","afterlimit":"-0.13","afteruppic":"-0.58","aftertime":"Mar 11 7:59PM EDT","ustime":"Mar 11 4:00PM EDT","chtime":"2013-03-12 07:58:04"}
     * gopicture : {"minurl":"http://image.sinajs.cn/newchartv5/usstock/min/aapl.gif","min_weekpic":"http://image.sinajs.cn/newchartv5/usstock/min_week/aapl.gif","dayurl":"http://image.sinajs.cn/newchartv5/usstock/daily/aapl.gif","weekurl":"http://image.sinajs.cn/newchartv5/usstock/weekly/aapl.gif","monthurl":"http://image.sinajs.cn/newchartv5/usstock/monthly/aapl.gif"}
     */

    public List<Result> result;

    public static class Result {
        /**
         * gid : aapl
         * name : 苹果
         * lastestpri : 437.87
         * openpri : 429.70
         * formpri : 431.72
         * maxpri : 439.01
         * minpri : 425.14
         * uppic : 6.15
         * limit : 1.42
         * traAmount : 16884191
         * avgTraNumber : 16910781
         * markValue : 411185326460
         * max52 : 705.07
         * min52 : 419.00
         * EPS : 44.16
         * priearn : 9.92
         * beta : 1.10
         * divident : 10.60
         * ROR : 10.24
         * capital : 939058000
         * afterpic : 437.29
         * afterlimit : -0.13
         * afteruppic : -0.58
         * aftertime : Mar 11 7:59PM EDT
         * ustime : Mar 11 4:00PM EDT
         * chtime : 2013-03-12 07:58:04
         */

        public Data data;
        /**
         * minurl : http://image.sinajs.cn/newchartv5/usstock/min/aapl.gif
         * min_weekpic : http://image.sinajs.cn/newchartv5/usstock/min_week/aapl.gif
         * dayurl : http://image.sinajs.cn/newchartv5/usstock/daily/aapl.gif
         * weekurl : http://image.sinajs.cn/newchartv5/usstock/weekly/aapl.gif
         * monthurl : http://image.sinajs.cn/newchartv5/usstock/monthly/aapl.gif
         */

        public Gopicture gopicture;

        public static class Data {
            public String gid;
            public String name;
            public String lastestpri;
            public String openpri;
            public String formpri;
            public String maxpri;
            public String minpri;
            public String uppic;
            public String limit;
            public String traAmount;
            public String avgTraNumber;
            public String markValue;
            public String max52;
            public String min52;
            public String EPS;
            public String priearn;
            public String beta;
            public String divident;
            public String ROR;
            public String capital;
            public String afterpic;
            public String afterlimit;
            public String afteruppic;
            public String aftertime;
            public String ustime;
            public String chtime;
        }

        public static class Gopicture {
            public String minurl;
            public String min_weekpic;
            public String dayurl;
            public String weekurl;
            public String monthurl;
        }
    }
}
