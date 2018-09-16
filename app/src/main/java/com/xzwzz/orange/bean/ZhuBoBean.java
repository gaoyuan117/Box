package com.xzwzz.orange.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/5/25.
 */

public class ZhuBoBean {

    private List<ZhuboBean> zhubo;

    public List<ZhuboBean> getZhubo() {
        return zhubo;
    }

    public void setZhubo(List<ZhuboBean> zhubo) {
        this.zhubo = zhubo;
    }

    public static class ZhuboBean {
        /**
         * address : rtmp://9180.liveplay.myqcloud.com/live/9180_899020
         * img : http://static.guojiang.tv/app/upload/2018_05_21/f2f6d25c8b7ad2029a228c0eed033dbb.jpg
         * title : 小蓉儿、啊%3F%3F
         */

        private String address;
        private String img;
        private String title;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

