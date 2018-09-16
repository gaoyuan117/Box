/*
 * PayConstants     2016/12/2 17:11
 * Copyright (c) 2016 Koterwong All right reserved
 */
package com.xzwzz.orange.pay.alipay.alipay;


import com.xzwzz.orange.AppConfig;

/**
 * Created by Koterwong on 2016/12/2 17:11
 */
public class Constants {
    public static class AliPay {

        public static boolean USE_RAS2 = true;

        /**
         * 支付宝AppID
         */
        public static String APPID = "2017063007605657";

        /**
         * 支付宝回调后台的NotifyUrl
         */
        public static String NOTIFY_URL = AppConfig.AP_LI_PAY_NOTIFY_URL;
        /**
         * 支付宝公钥
         */
        public static String RSA_PUBLIC = "";
        /**
         * 商户私钥，pkcs8格式
         */

        public static String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJEH8hPkWegcZ7iO4gfYEYJO/CrwsJutgIkyPie4LiT6nMbTvYTpVlOmIZiXzaubXy/eM6uuLDzN/UCdGo5KbFKXD76Ngw9GoL79s08rPnBrEyFaDk+kQ3XBxi5Ng2rajwR+lBgdbeAL6R2v7joUnHi0Zv6BJHdgc+KKdYzQukCrAgMBAAECgYAWhgdgc3xLAe5RyU7tLO87L40WFh83bjCiHf1tKbtw3sLuVf/+3kYb45cgHaNBXXXg9z9LEFeqccs896kiJAjCZt74oStAPt7vY9hY/rJo1PYeAz+HWHI7WhGN/DY1dv4g5uM0bX2O1mmN/ERcuYAb+6846M8veUT9M4qYFNBXoQJBAM1T3Z0Gn2q2L1/53nkecNifHTJo7VhGobcuaafHhXufXD+cayH0SXy4x2gOVIZEwuCoilOgaljxjy2l0lnyWSkCQQC00q3gcSIVyYEb2DSbGbjcE0GK27KvJghmoJ7TANJGcrQKv6dUqlMzxolYKh8Ov82tOwsXOn+cmae1wUzqXsGzAkA0tarWfLS/+DUBNmveO+pMMcyU/EqxtrAdDlUhNR8XNTnBOq5l8QhMdEL8e3FHZq+AbMPV5ABpBNn0pfTyEjzBAkEAhwN//MO8dZQdqNLIhL6x/vEJ0Uq1on29bBOmQ1qrpxpiQFxV7qu2sEQfrQrWYcbpJd2eLFvriNxaEyYf7ieObQJAJ+8CBhe3yc5+5z/VXShEgcryPrk1l6+SwJSDje8WPySyzLUyqcoz0Xz/ti6zlScdShhbLkIaO1jn5NCznsisGA==";

    }

    public static class WxPay

    {
        /**
         * 微信 AppID，在微信开放平台创建应用，并开通支付能力
         */
        public static String APP_ID = "wx83f74b4be5330249";
        /**
         * 商户号
         */
        public static String WX_SHOP_NUM = "1484420602";
        /**
         * 微信应用密钥
         */
        public static String KEY = "xiaoyuanshenghuoxiaoyuanshenghuo";

        /** Secret */
        public static String SECRET = "648c7c2d14259d1a14d0bf82201931d0";

        public static String NOTIFY_URL = AppConfig.WX_PAY_NOTIFY_URL;
    }
}
