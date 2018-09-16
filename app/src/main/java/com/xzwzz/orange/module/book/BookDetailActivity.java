package com.xzwzz.orange.module.book;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseObjObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.BookBean;
import com.xzwzz.orange.utils.StatusBarUtil;

public class BookDetailActivity extends AppCompatActivity {

    private android.webkit.WebView mWebview;
    private String id;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initView();
        initData();
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    protected void initView() {
        mWebview = findViewById(R.id.webview);
        tvTitle = findViewById(R.id.tv_title);
        StatusBarUtil.getInstance().setPaddingSmart(this, mWebview);
    }

    protected void initData() {
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        tvTitle.setText(name);

        id = extras.getString("id");
        RetrofitClient.getInstance().createApi().getNoveldetails("Home.Noveldetails", id)
                .compose(RxUtils.io_main())
                .subscribe(new BaseObjObserver<BookBean>(ProgressDialog.show(this, "", "加载中")) {
                    @Override
                    protected void onHandleSuccess(BookBean obj) {
                        String linkCss = "<style type=\"text/css\"> " +
                                "img {" +
                                "width:100%;" +
                                "height:auto;" +
                                "}" +
                                "</style>";
                        String html = "\n<html><header>" + linkCss + "</header>" + obj.post_content + "</body></html>";
                        mWebview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                    }
                });
    }

}
