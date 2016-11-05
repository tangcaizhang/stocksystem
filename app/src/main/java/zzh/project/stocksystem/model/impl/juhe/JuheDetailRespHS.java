package zzh.project.stocksystem.model.impl.juhe;

import java.util.List;

public class JuheDetailRespHS {

    /**
     * resultcode : 200
     * reason : SUCCESSED!
     * result : [{"data":{"gid":"sh601009","increPer":"9.91","increase":"43.99","name":"南京银行","todayStartPri":"8.26","yestodEndPri":"8.26","nowPri":"8.37","todayMax":"8.55","todayMin":"8.25","competitivePri":"8.37","reservePri":"8.38","traNumber":"34501453","traAmount":"290889560","buyOne":"10870","buyOnePri":"8.37","buyTwo":"177241","buyTwoPri":"8.36","buyThree":"92600","buyThreePri":"8.35","buyFour":"87200","buyFourPri":"8.34","buyFive":"113700","buyFivePri":"8.42","sellOne":"47556","sellOnePri":"8.38","sellTwo":"103057","sellTwoPri":"8.39","sellThree":"186689","sellThreePri":"8.40","sellFour":"49000","sellFourPri":"8.41","sellFive":"214535","sellFivePri":"15.21","date":"2012-12-11","time":"15:03:06"},"dapandata":{},"gopicture":{"minurl":"http://image.sinajs.cn/newchart/min/n/sh601009.gif","dayurl":"http://image.sinajs.cn/newchart/daily/n/sh601009.gif","weekurl":"http://image.sinajs.cn/newchart/weekly/n/sh601009.gif","monthurl":"http://image.sinajs.cn/newchart/monthly/n/sh601009.gif"}}]
     */

    public String resultcode;
    public String reason;
    /**
     * data : {"gid":"sh601009","increPer":"9.91","increase":"43.99","name":"南京银行","todayStartPri":"8.26","yestodEndPri":"8.26","nowPri":"8.37","todayMax":"8.55","todayMin":"8.25","competitivePri":"8.37","reservePri":"8.38","traNumber":"34501453","traAmount":"290889560","buyOne":"10870","buyOnePri":"8.37","buyTwo":"177241","buyTwoPri":"8.36","buyThree":"92600","buyThreePri":"8.35","buyFour":"87200","buyFourPri":"8.34","buyFive":"113700","buyFivePri":"8.42","sellOne":"47556","sellOnePri":"8.38","sellTwo":"103057","sellTwoPri":"8.39","sellThree":"186689","sellThreePri":"8.40","sellFour":"49000","sellFourPri":"8.41","sellFive":"214535","sellFivePri":"15.21","date":"2012-12-11","time":"15:03:06"}
     * dapandata : {}
     * gopicture : {"minurl":"http://image.sinajs.cn/newchart/min/n/sh601009.gif","dayurl":"http://image.sinajs.cn/newchart/daily/n/sh601009.gif","weekurl":"http://image.sinajs.cn/newchart/weekly/n/sh601009.gif","monthurl":"http://image.sinajs.cn/newchart/monthly/n/sh601009.gif"}
     */

    public List<Result> result;

    public static class Result {
        /**
         * gid : sh601009
         * increPer : 9.91
         * increase : 43.99
         * name : 南京银行
         * todayStartPri : 8.26
         * yestodEndPri : 8.26
         * nowPri : 8.37
         * todayMax : 8.55
         * todayMin : 8.25
         * competitivePri : 8.37
         * reservePri : 8.38
         * traNumber : 34501453
         * traAmount : 290889560
         * buyOne : 10870
         * buyOnePri : 8.37
         * buyTwo : 177241
         * buyTwoPri : 8.36
         * buyThree : 92600
         * buyThreePri : 8.35
         * buyFour : 87200
         * buyFourPri : 8.34
         * buyFive : 113700
         * buyFivePri : 8.42
         * sellOne : 47556
         * sellOnePri : 8.38
         * sellTwo : 103057
         * sellTwoPri : 8.39
         * sellThree : 186689
         * sellThreePri : 8.40
         * sellFour : 49000
         * sellFourPri : 8.41
         * sellFive : 214535
         * sellFivePri : 15.21
         * date : 2012-12-11
         * time : 15:03:06
         */

        public Data data;
        /**
         * minurl : http://image.sinajs.cn/newchart/min/n/sh601009.gif
         * dayurl : http://image.sinajs.cn/newchart/daily/n/sh601009.gif
         * weekurl : http://image.sinajs.cn/newchart/weekly/n/sh601009.gif
         * monthurl : http://image.sinajs.cn/newchart/monthly/n/sh601009.gif
         */

        public Gopicture gopicture;

        public static class Data {
            public String gid;
            public String increPer;
            public String increase;
            public String name;
            public String todayStartPri;
            public String yestodEndPri;
            public String nowPri;
            public String todayMax;
            public String todayMin;
            public String competitivePri;
            public String reservePri;
            public String traNumber;
            public String traAmount;
            public String buyOne;
            public String buyOnePri;
            public String buyTwo;
            public String buyTwoPri;
            public String buyThree;
            public String buyThreePri;
            public String buyFour;
            public String buyFourPri;
            public String buyFive;
            public String buyFivePri;
            public String sellOne;
            public String sellOnePri;
            public String sellTwo;
            public String sellTwoPri;
            public String sellThree;
            public String sellThreePri;
            public String sellFour;
            public String sellFourPri;
            public String sellFive;
            public String sellFivePri;
            public String date;
            public String time;
        }

        public static class Gopicture {
            public String minurl;
            public String dayurl;
            public String weekurl;
            public String monthurl;
        }
    }
}
