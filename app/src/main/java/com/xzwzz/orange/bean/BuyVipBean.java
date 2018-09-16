package com.xzwzz.orange.bean;

import java.util.List;

/**
 * Project Name :phonelive
 * package Name :com.vlive.phonelive.bean
 * Author       :xzwzz@vip.QqBean.com
 * Createtime   :2017/9/25  15:41
 */
public class BuyVipBean {

    public String transfer_switch;
    public String aliapp_switch;
    public String aliapp_partner;
    public String aliapp_seller_id;
    public String aliapp_key;
    public String wx_switch;
    public String wx_appid;
    public String wx_appsecret;
    public String wx_mchid;
    public String wx_key;
    public List<ListBean> list;

    public static class ListBean {
        public ListBean(String name, String coin) {
            this.name = name;
            this.coin = coin;
        }

        public String id;
        public String name;
        public String coin;
        public String month;
        public String addtime;
        public String orderno;
    }
}
