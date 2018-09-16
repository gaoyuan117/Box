package com.xzwzz.orange.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/5/11.
 */

public class VipBean {

    /**
     * ret : 200
     * data : {"code":0,"msg":"操作成功","info":[0]}
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
         * msg : 操作成功
         * info : [0]
         */

        private int code;
        private String msg;
        private List<Integer> info;

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

        public List<Integer> getInfo() {
            return info;
        }

        public void setInfo(List<Integer> info) {
            this.info = info;
        }
    }
}
