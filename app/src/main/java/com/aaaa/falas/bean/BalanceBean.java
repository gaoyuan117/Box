package com.aaaa.falas.bean;

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
public class BalanceBean {

    public String coin;
    public String aliapp_switch;
    public String aliapp_partner;
    public String aliapp_seller_id;
    public String aliapp_key_android;
    public String aliapp_key_ios;
    public String wx_switch;
    public String wx_appid;
    public String wx_appsecret;
    public String wx_mchid;
    public String wx_key;
    public List<RulesBean> rules;

    public static class RulesBean {
        public String id;
        public String coin;
        public String money;
        public String money_ios;
        public String product_id;
        public String give;
    }
}
