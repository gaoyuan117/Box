package com.xzwzz.orange.bean;

import java.util.List;

public class VideoDetailBean {
    /**
     * details : {"id":"12","title":"小草资源网","img_url":"http://ozk3zmyhw.bkt.clouddn.com/20180711_5b45b6df06596.jpg","url":"http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4","uptime":"2018-08-07 10:17:27","listorder":"5","coin":"14","watch_num":"5","is_buy":0}
     * list : [{"id":"14","title":"在线宅男","img_url":"http://ozk3zmyhw.bkt.clouddn.com/20180711_5b45b9bf4ebdd.jpg","url":"http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4","uptime":"2018-08-07 10:17:34","listorder":"7","coin":"10","watch_num":"2"}]
     */

    private DetailsBean details;
    private List<ListBean> list;

    public DetailsBean getDetails() {
        return details;
    }

    public void setDetails(DetailsBean details) {
        this.details = details;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class DetailsBean {
        /**
         * id : 12
         * title : 小草资源网
         * img_url : http://ozk3zmyhw.bkt.clouddn.com/20180711_5b45b6df06596.jpg
         * url : http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4
         * uptime : 2018-08-07 10:17:27
         * listorder : 5
         * coin : 14
         * watch_num : 5
         * is_buy : 0
         */

        private String id;
        private String title;
        private String img_url;
        private String url;
        private String video_url;
        private String uptime;
        private String listorder;
        private String coin;
        private String watch_num;
        private String video_img;
        private int is_buy;

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getVideo_img() {
            return video_img;
        }

        public void setVideo_img(String video_img) {
            this.video_img = video_img;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }

        public String getListorder() {
            return listorder;
        }

        public void setListorder(String listorder) {
            this.listorder = listorder;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getWatch_num() {
            return watch_num;
        }

        public void setWatch_num(String watch_num) {
            this.watch_num = watch_num;
        }

        public int getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(int is_buy) {
            this.is_buy = is_buy;
        }
    }

    public static class ListBean {
        /**
         * id : 14
         * title : 在线宅男
         * img_url : http://ozk3zmyhw.bkt.clouddn.com/20180711_5b45b9bf4ebdd.jpg
         * url : http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4
         * uptime : 2018-08-07 10:17:34
         * listorder : 7
         * coin : 10
         * watch_num : 2
         */

        private String id;
        private String title;
        private String img_url;
        private String url;
        private String uptime;
        private String listorder;
        private String coin;
        private String watch_num;
        private String video_img;
        private int is_buy;

        public String getVideo_img() {
            return video_img;
        }

        public void setVideo_img(String video_img) {
            this.video_img = video_img;
        }

        public int getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(int is_buy) {
            this.is_buy = is_buy;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }

        public String getListorder() {
            return listorder;
        }

        public void setListorder(String listorder) {
            this.listorder = listorder;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getWatch_num() {
            return watch_num;
        }

        public void setWatch_num(String watch_num) {
            this.watch_num = watch_num;
        }
    }
}

