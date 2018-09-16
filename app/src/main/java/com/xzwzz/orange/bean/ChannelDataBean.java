package com.xzwzz.orange.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaoyuan on 2017/11/23.
 */

public class ChannelDataBean implements Serializable {
    private int ret;
    private List<DataBean> data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private int channel_id;
        private long uid;
        private String name;
        private String bigpic;
        private String num;
        private String url;
        private int today_create;
        private String live_state;
        private String cookie;
        private String appid;
        private String userid;
        private int type;
        private int liveurl;
        private String doman;

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBigpic() {
            return bigpic;
        }

        public void setBigpic(String bigpic) {
            this.bigpic = bigpic;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getToday_create() {
            return today_create;
        }

        public void setToday_create(int today_create) {
            this.today_create = today_create;
        }

        public String getLive_state() {
            return live_state;
        }

        public void setLive_state(String live_state) {
            this.live_state = live_state;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getLiveurl() {
            return liveurl;
        }

        public void setLiveurl(int liveurl) {
            this.liveurl = liveurl;
        }

        public String getDoman() {
            return doman;
        }

        public void setDoman(String doman) {
            this.doman = doman;
        }
    }
}
