package com.xzwzz.orange.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/6/3.
 */

public class QqBean {


    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"qq":"QQ111111111","yueka_url":null,"jika_url":null,"nianka_url":null,"zhongshenka_url":null}]}
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
         * info : [{"qq":"QQ111111111","yueka_url":null,"jika_url":null,"nianka_url":null,"zhongshenka_url":null}]
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
             * qq : QQ111111111
             * yueka_url : null
             * jika_url : null
             * nianka_url : null
             * zhongshenka_url : null
             */

            private String qq;
            private String yueka_url;
            private String jika_url;
            private String nianka_url;
            private String zhongshenka_url;
            private String invitation_code;

            public String getInvitation_code() {
                return invitation_code;
            }

            public void setInvitation_code(String invitation_code) {
                this.invitation_code = invitation_code;
            }

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }

            public String getYueka_url() {
                return yueka_url;
            }

            public void setYueka_url(String yueka_url) {
                this.yueka_url = yueka_url;
            }

            public String getJika_url() {
                return jika_url;
            }

            public void setJika_url(String jika_url) {
                this.jika_url = jika_url;
            }

            public String getNianka_url() {
                return nianka_url;
            }

            public void setNianka_url(String nianka_url) {
                this.nianka_url = nianka_url;
            }

            public String getZhongshenka_url() {
                return zhongshenka_url;
            }

            public void setZhongshenka_url(String zhongshenka_url) {
                this.zhongshenka_url = zhongshenka_url;
            }
        }
    }
}
