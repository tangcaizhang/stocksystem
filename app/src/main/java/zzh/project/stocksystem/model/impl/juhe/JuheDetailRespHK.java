package zzh.project.stocksystem.model.impl.juhe;

import java.util.List;

public class JuheDetailRespHK {

    /**
     * resultcode : 200
     * reason : SUCCESSED!
     * result : [{"data":{"gid":"hk00001","ename":"CHEUNG KONG","name":"长江实业","openpri":"119.600","formpri":"119.200","maxpri":"120.600","minpri":"119.600","lastestpri":"119.800","uppic":"0.600","limit":"0.503","inpic":"119.700","outpic":"119.900","traAmount":"121700502","traNumber":"1014242","priearn":"6.025","max52":"132.700","min52":"86.000","date":"2013/03/12","time":"11:19:12"},"gopicture":{"minurl":"http://image.sinajs.cn/newchart/hk_stock/min/00001.gif","dayurl":"http://image.sinajs.cn/newchart/hk_stock/daily/00001.gif","weekurl":"http://image.sinajs.cn/newchart/hk_stock/weekly/00001.gif","monthurl":"http://image.sinajs.cn/newchart/hk_stock/monthly/00001.gif"},"hengsheng_data":{"date":"2014/05/12","formpri":"21862.99","lastestpri":"22220.65","limit":"1.64","max52":"24111.55","maxpri":"22250.28","min52":"0.00","minpri":"21796.90","openpri":"21921.59","time":"11:44:00","traAmount":"31524616000","uppic":"357.66"}}]
     */

    public String resultcode;
    public String reason;
    /**
     * data : {"gid":"hk00001","ename":"CHEUNG KONG","name":"长江实业","openpri":"119.600","formpri":"119.200","maxpri":"120.600","minpri":"119.600","lastestpri":"119.800","uppic":"0.600","limit":"0.503","inpic":"119.700","outpic":"119.900","traAmount":"121700502","traNumber":"1014242","priearn":"6.025","max52":"132.700","min52":"86.000","date":"2013/03/12","time":"11:19:12"}
     * gopicture : {"minurl":"http://image.sinajs.cn/newchart/hk_stock/min/00001.gif","dayurl":"http://image.sinajs.cn/newchart/hk_stock/daily/00001.gif","weekurl":"http://image.sinajs.cn/newchart/hk_stock/weekly/00001.gif","monthurl":"http://image.sinajs.cn/newchart/hk_stock/monthly/00001.gif"}
     * hengsheng_data : {"date":"2014/05/12","formpri":"21862.99","lastestpri":"22220.65","limit":"1.64","max52":"24111.55","maxpri":"22250.28","min52":"0.00","minpri":"21796.90","openpri":"21921.59","time":"11:44:00","traAmount":"31524616000","uppic":"357.66"}
     */

    public List<Result> result;

    public static class Result {
        /**
         * gid : hk00001
         * ename : CHEUNG KONG
         * name : 长江实业
         * openpri : 119.600
         * formpri : 119.200
         * maxpri : 120.600
         * minpri : 119.600
         * lastestpri : 119.800
         * uppic : 0.600
         * limit : 0.503
         * inpic : 119.700
         * outpic : 119.900
         * traAmount : 121700502
         * traNumber : 1014242
         * priearn : 6.025
         * max52 : 132.700
         * min52 : 86.000
         * date : 2013/03/12
         * time : 11:19:12
         */

        public Data data;
        /**
         * minurl : http://image.sinajs.cn/newchart/hk_stock/min/00001.gif
         * dayurl : http://image.sinajs.cn/newchart/hk_stock/daily/00001.gif
         * weekurl : http://image.sinajs.cn/newchart/hk_stock/weekly/00001.gif
         * monthurl : http://image.sinajs.cn/newchart/hk_stock/monthly/00001.gif
         */

        public Gopicture gopicture;
        /**
         * date : 2014/05/12
         * formpri : 21862.99
         * lastestpri : 22220.65
         * limit : 1.64
         * max52 : 24111.55
         * maxpri : 22250.28
         * min52 : 0.00
         * minpri : 21796.90
         * openpri : 21921.59
         * time : 11:44:00
         * traAmount : 31524616000
         * uppic : 357.66
         */

        public HengshengData hengsheng_data;

        public static class Data {
            public String gid;
            public String ename;
            public String name;
            public String openpri;
            public String formpri;
            public String maxpri;
            public String minpri;
            public String lastestpri;
            public String uppic;
            public String limit;
            public String inpic;
            public String outpic;
            public String traAmount;
            public String traNumber;
            public String priearn;
            public String max52;
            public String min52;
            public String date;
            public String time;
        }

        public static class Gopicture {
            public String minurl;
            public String dayurl;
            public String weekurl;
            public String monthurl;
        }

        public static class HengshengData {
            public String date;
            public String formpri;
            public String lastestpri;
            public String limit;
            public String max52;
            public String maxpri;
            public String min52;
            public String minpri;
            public String openpri;
            public String time;
            public String traAmount;
            public String uppic;
        }
    }
}
