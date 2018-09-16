package com.xzwzz.orange.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.BaseBean;
import com.xzwzz.orange.bean.ConfigBean;
import com.xzwzz.orange.bean.QqBean;
import com.xzwzz.orange.bean.UserInfoBean;
import com.xzwzz.orange.module.live.LiveChannelActivity;
import com.xzwzz.orange.ui.adapter.MainFragmentAdapter;
import com.xzwzz.orange.ui.fragment.HomeFragment;
import com.xzwzz.orange.ui.fragment.MainFragment;
import com.xzwzz.orange.ui.fragment.MineFragment;
import com.xzwzz.orange.ui.fragment.PromotionFragment;
import com.xzwzz.orange.ui.fragment.YunBoFragment;
import com.xzwzz.orange.utils.DialogHelp;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.utils.UpdateManager;
import com.xzwzz.orange.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    private NoScrollViewPager mViewpagerMain;
    private com.xzwzz.orange.widget.CustomBottomNavigationView mNavigationMain;


    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        mViewpagerMain = findViewById(R.id.viewpager_main);
        mNavigationMain = findViewById(R.id.navigation_main);
        mNavigationMain.setBackgroundColor(getResources().getColor(R.color.white));

//        if (AppConfig.STATUS == 1) {//弹出对话框
//            showDialog();
//
//        }

        List<Fragment> list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new YunBoFragment());
        list.add(new MineFragment());
        mViewpagerMain.setSlidingEnable(false);
        mViewpagerMain.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), list));
        mViewpagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNavigationMain.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mNavigationMain.enableAnimation(false);
        mNavigationMain.enableItemShiftingMode(false);
        mNavigationMain.enableShiftingMode(false);
        int[][] states = new int[][]{new int[]{-android.R.attr.state_checked}, new int[]{android.R.attr.state_checked}};

        int[] colors = new int[]{Color.parseColor("#4D4D4D"), Color.parseColor("#ed4d45")
        };
        ColorStateList csl = new ColorStateList(states, colors);
        mNavigationMain.setItemIconTintList(csl);
        mNavigationMain.setItemTextColor(csl);

        mNavigationMain.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btn_bottom_main:
                    mViewpagerMain.setCurrentItem(0);
                    return true;
                case R.id.btn_bottom_promotion:
                    mViewpagerMain.setCurrentItem(1);
                    return true;
                case R.id.btn_bottom_mine:
                    mViewpagerMain.setCurrentItem(2);
                    return true;
                default:
                    break;
            }
            return false;
        });
    }

    @Override
    protected void initData() {
        init();
        check();

        RetrofitClient.getInstance().createApi().getBaseUserInfo("User.getBaseInfo", AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<UserInfoBean>() {
                    @Override
                    protected void onHandleSuccess(List<UserInfoBean> list) {
                        if (list.size() > 0) {
                            UserInfoBean bean = list.get(0);
                            AppConfig.IS_MEMBER = (bean.is_member == 1);
                        }
                    }
                });
        LoginUtils.tokenIsOutTime(new BaseListObserver<BaseBean>() {
            @Override
            protected void onHandleSuccess(List<BaseBean> list) {
                if (list != null) {

                }
            }
        });

    }

    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime > 2000) {
            ToastUtils.showShort("再按一次退出程序");
            firstTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    private final static String[] AUTH_BASE_ARR =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA
                    , Manifest.permission.READ_PHONE_STATE};
    private final static int AUTH_BASE_REQUEST_CODE = 1;
    private final static int AUTH_COM_REQUEST_CODE = 2;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AUTH_BASE_REQUEST_CODE) {
            for (int ret : grantResults) {
                Log.d("GuideActivity", "ret:" + ret);
                if (ret == 0) {
                    continue;
                } else {
//                    ToastUtils.showShort("缺少导航基本的权限");
                    return;
                }
            }
            init();
        } else if (requestCode == AUTH_COM_REQUEST_CODE) {
            ToastUtils.showShort("初始化完毕");
        }
    }

    private void init() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(AUTH_BASE_ARR, AUTH_BASE_REQUEST_CODE);
                return;
            }
        }
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : AUTH_BASE_ARR) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void check() {
        if (AppConfig.MAINTAIN_SWITCC == 1) {
            String maintain_tips = AppConfig.maintain_tips;
            try {
                DialogHelp.showMainTainDialog(MainActivity.this, maintain_tips);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        new UpdateManager(MainActivity.this, AppConfig.apk_ver, AppConfig.apk_url, false).checkUpdate();
    }


    private void showDialog() {
        DialogHelp.showMainDialog(this, AppConfig.QQCONTENT, (View.OnClickListener) v -> {

            joinQQGroup(AppConfig.GROUPKEY);

        }).show();
    }


    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}
