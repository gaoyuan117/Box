/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014. Ray
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.xzwzz.orange.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 */
public class MD5Utils {

    public static String md5(byte[] data) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(data, 0, data.length);
//            byte[] digest = md.digest();
//            return convertHashString(digest);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        return new String(data);
    }

    private static String convertHashString(byte[] hashBytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : hashBytes) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            android.util.Log.d("jason", "md5 算法不支持!");
        }
    }


    public static String toMD5(String val) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes());
            byte[] m = md5.digest();//加密
            return getString(m);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int num = b[i] & 0xff; // 这里的是为了将原本是byte型的数向上提升为int型，从而使得原本的负数转为了正数
            String hex = Integer.toHexString(num); //这里将int型的数直接转换成16进制表示
            if (hex.length() == 1) {
                sb.append(0);
            }
            sb.append(hex);
        }
        Log.e("gy", "sb：" + sb.toString());
        String s = sb.toString().toUpperCase();
        return s;
    }
}
