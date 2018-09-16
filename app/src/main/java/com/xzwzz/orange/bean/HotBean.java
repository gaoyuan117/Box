package com.xzwzz.orange.bean;

import java.util.List;

//
//                          _oo0oo_
//                         o8888888o
//                          88" . "88
//                          (| -_- |)
//                          0\  =  /0
//                      ___/`---'\___
//                      .' \\|     |// '.
//                   / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//                  |   | \\\  -  /// |   |
//                  | \_|  ''\---/''  |_/ |
//                  \  .-\__  '-'  ___/-. /
//               ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//          \  \ `_.   \_ __\ /__ _/   .-` /  /
//=====`-.____`.___ \_____/___.-`___.-'=====
//                           `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
public class HotBean {


    public ListBean list;
    public List<BannerBean> slide;

    public static class ListBean {
        public List<DataBean> data;

        public static class DataBean {
            public String id;
            public String title;
            public String video_img;
            public String video_url;
            public String uptime;
            public String term_id;
            public String user_nicename;
            public String avatar_thumb;
            public Object thumb;
            public int type;
            public int uid;
        }
    }

    public static class BannerBean {
        public String slide_pic;
        public String slide_url;
    }
}
