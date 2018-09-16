package com.xzwzz.orange.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.xzwzz.orange.bean.NovelTermBean;
import com.xzwzz.orange.bean.UserInfoBean;
import com.xzwzz.orange.ui.adapter.ViewPagerAdapter;
import com.xzwzz.orange.ui.fragment.GameFragment;
import com.xzwzz.orange.ui.fragment.HomeFragment;
import com.xzwzz.orange.ui.fragment.MineFragment;
import com.xzwzz.orange.ui.fragment.YingFragment;
import com.xzwzz.orange.ui.fragment.YunBoFragment;
import com.xzwzz.orange.ui.fragment.YunSearchFragment;
import com.xzwzz.orange.utils.DialogHelp;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.utils.UpdateManager;
import com.xzwzz.orange.view.AlphaTabView;
import com.xzwzz.orange.view.AlphaTabsIndicator;
import com.xzwzz.orange.view.OnTabChangedListner;
import com.xzwzz.orange.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements OnTabChangedListner {

    NoScrollViewPager viewPager;
    AlphaTabsIndicator mAlphaTabsIndicator;
    AlphaTabView tabMine;

    private List<Fragment> mList;
    private ViewPagerAdapter adapter;
    private long firstTime = 0;

    private final static String[] AUTH_BASE_ARR =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA
                    , Manifest.permission.READ_PHONE_STATE};
    private final static int AUTH_BASE_REQUEST_CODE = 1;
    private final static int AUTH_COM_REQUEST_CODE = 2;
    private int current = 0;

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.view_pager);
        mAlphaTabsIndicator = findViewById(R.id.alphaIndicator);
        tabMine = findViewById(R.id.tab_mine);

//        StatusBarUtil.getInstance().darkMode(this);

    }

    @Override
    public void initData() {
        viewPager = findViewById(R.id.view_pager);
        mAlphaTabsIndicator = findViewById(R.id.alphaIndicator);
        viewPager.setOffscreenPageLimit(4);
        mList = new ArrayList<>();

        mList.add(new HomeFragment());
//        mList.add(new YunBoFragment());
        mList.add(new AvFragment());
        mList.add(new YingFragment());
        mList.add(new MineFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, mList);
        viewPager.setAdapter(adapter);

        mAlphaTabsIndicator.setViewPager(viewPager);
        mAlphaTabsIndicator.setOnTabChangedListner(this);
        mAlphaTabsIndicator.removeAllBadge();


        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type) && type.equals("message")) {
            if (viewPager == null || mAlphaTabsIndicator == null) {
                return;
            }
            viewPager.setCurrentItem(1);
            mAlphaTabsIndicator.setTabCurrenItem(1);
        }

        init();
        check();

        LoginUtils.tokenIsOutTime(new BaseListObserver<BaseBean>() {
            @Override
            protected void onHandleSuccess(List<BaseBean> list) {
                if (list != null) {

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (LoginUtils.isWifiProxy(this)) {
            finish();
        }

        isMember();

    }

    @Override
    public void onTabSelected(int tabNum) {

        viewPager.setCurrentItem(tabNum);
        current = tabNum;


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra("exit", false);
            if (isExit) {
                this.finish();
            }
        }
    }

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
                DialogHelp.showMainTainDialog(HomeActivity.this, maintain_tips);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        new UpdateManager(HomeActivity.this, AppConfig.apk_ver, AppConfig.apk_url, false).checkUpdate();
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

    //是否为会员
    private void isMember() {
        String loginUid = AppContext.getInstance().getLoginUid();
        if (loginUid.equals("0")) return;
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
    }

}
