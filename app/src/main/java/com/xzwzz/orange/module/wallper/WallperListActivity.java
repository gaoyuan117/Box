package com.xzwzz.orange.module.wallper;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.api.http.BaseObjObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.WallperListBean;
import com.xzwzz.orange.module.AbsModuleActivity;

//
//                          _oo0oo_
//                         o8888888o
//                          88" . "88
//                          (| -_- |)
//                          0\  =  /0
//                      ___/`---'\___
//                      .' \\|     |// '.
//                   / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//                  |   | \\\  -  /// |   |
//                  | \_|  ''\---/''  |_/ |
//                  \  .-\__  '-'  ___/-. /
//               ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//          \  \ `_.   \_ __\ /__ _/   .-` /  /
//=====`-.____`.___ \_____/___.-`___.-'=====
//                           `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
public class WallperListActivity extends AbsModuleActivity {
    private String id;

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new WallperListAdapter();
    }

    @Override
    protected void initView() {
        super.initView();
        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title");
        id = extras.getString("id");
        setToolbar(title, true);
    }


    @Override
    protected void loadData() {
        RetrofitClient.getInstance().createApi().getWallperList("Home.Picturedetails", id)
                .compose(RxUtils.io_main())
                .subscribe(new BaseObjObserver<WallperListBean>(mViewStatusManager, mSwipeRefreshLayout) {
                    @Override
                    protected void onHandleSuccess(WallperListBean obj) {
                        mAdapter.setNewData(obj.photos_url);
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
