package com.xzwzz.orange.api.http;

import java.util.List;

/**
 * Created by admin on 2017/3/27.
 */

public class HttpArray<T> {

    public int ret;
    public DataBean<T> data;
    public String msg;

    public static class DataBean<T> {
        public int code;
        public String msg;
        public List<T> info;
    }
}
