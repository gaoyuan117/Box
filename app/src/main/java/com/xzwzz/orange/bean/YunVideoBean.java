package com.xzwzz.orange.bean;

import java.util.List;

public class YunVideoBean {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"id":"2","video_url":"https://d2.xia12345.com/down/88/2018/04/bCXCkLWG.mp4","uid":null,"uptime":"2018-04-20 22:33:35","term_id":"3","title":"KIKI","video_img":"https://pppp.642p.com/88/2018/04/bCXCkLWG.gif"}]}
     * msg :
     */

    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * code : 0
         * msg :
         * info : [{"id":"2","video_url":"https://d2.xia12345.com/down/88/2018/04/bCXCkLWG.mp4","uid":null,"uptime":"2018-04-20 22:33:35","term_id":"3","title":"KIKI","video_img":"https://pppp.642p.com/88/2018/04/bCXCkLWG.gif"}]
         */

        private int code;
        private String msg;
        private List<InfoBean> info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 2
             * video_url : https://d2.xia12345.com/down/88/2018/04/bCXCkLWG.mp4
             * uid : null
             * uptime : 2018-04-20 22:33:35
             * term_id : 3
             * title : KIKI
             * video_img : https://pppp.642p.com/88/2018/04/bCXCkLWG.gif
             */

            private String id;
            private String video_url;
            private Object uid;
            private String uptime;
            private String term_id;
            private String title;
            private String video_img;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }

            public Object getUid() {
                return uid;
            }

            public void setUid(Object uid) {
                this.uid = uid;
            }

            public String getUptime() {
                return uptime;
            }

            public void setUptime(String uptime) {
                this.uptime = uptime;
            }

            public String getTerm_id() {
                return term_id;
            }

            public void setTerm_id(String term_id) {
                this.term_id = term_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVideo_img() {
                return video_img;
            }

            public void setVideo_img(String video_img) {
                this.video_img = video_img;
            }
        }
    }
}
