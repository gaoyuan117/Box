package com.aaaa.falas.api.http;

/**
 * Created by admin on 2017/3/27.
 */

public class HttpResult<T> {
    public int ret;
    public DataBean<T> data;
    public String msg;

    public static class DataBean<T> {
        public int code;
        public String msg;
        public T info;
    }
}
