package com.xzwzz.orange;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.blankj.utilcode.util.AppUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 */
public class AppConfig {
    //    public static final String MAIN_URL = "http://hezi.jf7e.cn/";
//    public static final String MAIN_URL = "http://mangguo.8eym3.cn/";
    public static final String MAIN_URL = "http://fala.8eym3.cn/";
    //api地址
    public static final String MAIN_URL_API = MAIN_URL + "/api/public/";
    //支付宝回调地址
    public static final String AP_LI_PAY_NOTIFY_URL = MAIN_URL + "/index.php/Appapi/Notify/aliPay";
    //微信回调地址
    public static final String WX_PAY_NOTIFY_URL = MAIN_URL + "/index.php/Appapi/Notify/WXPay";

    public static final String IMG_URL = MAIN_URL + "/upload/";


    public static final boolean openOtherLogin = false;//是否开启第三方
    public static final boolean openModifyInformation = false;


    public static String SHARE_TITLE = "";
    public static int MAINTAIN_SWITCC;//是否维护
    public static String apk_ver;//版本号
    public static String apk_url;//下载地址
    public static String video_url;//解析地址
    public static String maintain_tips;//维护内容
    public static String VIDEO_URL;//维护内容
    public static String SHARE_DES = "";
    public static String QQ = "";
    public static String YUE = "";
    public static String JI = "";
    public static String YEAR = "";
    public static String FOREVER = "";
    public static String CODE = "";
    public static int STATUS = 1;
    public static String QQCONTENT = "";
    public static String GROUPKEY = "";
    public static String GROUP = "";

    public static String GLOBAL_WX_KEY = "";//微信KEY
    public static String TICK_NAME = ""; //魅力值昵称

    public static String CURRENCY_NAME = ""; //金币
    public static int JOIN_ROOM_ANIMATION_LEVEL;//进场动画等级限制

    public static int SEND_PUBLIC_CHAT_LEVEL = -1;//发送公屏等级限制

    public static int ROOM_CHARGE_SWITCH = 0;//收费房间开关·
    public static int ROOM_PASSWORD_SWITCH = 0;//密码房间开关
    public static int ROOM_TIME_SWITCH = 0;//计时收费开关

    public static String APP_ANDROID_SHARE = "";

    public static boolean IS_MEMBER = false;

    //获取鉴权串，demo里为testAppServer，请改用自己的appserver
    public static final String RTC_AUTH_SERVER = "http://rtc.vcloud.ks-live.com:6002/rtcauth";

    private final static String APP_CONFIG = "config";

    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";

    public static final String KEY_TWEET_DRAFT = "KEY_TWEET_DRAFT";

    public static final String KEY_NOTE_DRAFT = "KEY_NOTE_DRAFT";

    public static final String KEY_FRITST_START = "KEY_FRIST_START";

    public static final String KEY_NIGHT_MODE_SWITCH = "night_mode_switch";

    public static final int USER_INFO_MAXLEN = 20;


    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + AppUtils.getAppPackageName()
            + File.separator + "live_img" + File.separator;

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + AppUtils.getAppPackageName()
            + File.separator + "download" + File.separator;


    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key) {
            props.remove(k);
        }
        setProps(props);
    }
}
