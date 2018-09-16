package com.xzwzz.orange.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/9/16.
 */

public class ChatBean {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[[{"id":"1","content":"直播消息：流氓来了","addtime":"1536765958"},{"id":"2","content":"啊哈哈：美女厉害","addtime":"1536765985"}]]}
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
         * info : [[{"id":"1","content":"直播消息：流氓来了","addtime":"1536765958"},{"id":"2","content":"啊哈哈：美女厉害","addtime":"1536765985"}]]
         */

        private int code;
        private String msg;
        private List<List<InfoBean>> info;

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

        public List<List<InfoBean>> getInfo() {
            return info;
        }

        public void setInfo(List<List<InfoBean>> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 1
             * content : 直播消息：流氓来了
             * addtime : 1536765958
             */

            private String id;
            private String content;
            private String addtime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
        }
    }
}
