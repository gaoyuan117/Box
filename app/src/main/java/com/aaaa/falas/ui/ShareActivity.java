package com.aaaa.falas.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.aaaa.falas.AppConfig;
import com.aaaa.falas.AppContext;
import com.aaaa.falas.R;


//推荐还有

public class ShareActivity extends AppCompatActivity {


    private TextView tvCode, tvLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        findViewById(R.id.img_back).setOnClickListener(view -> finish());
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("推荐好友");

        tvCode = findViewById(R.id.tv_code);
        tvLink = findViewById(R.id.tv_link);

        if (TextUtils.isEmpty(AppConfig.CODE)){
            tvCode.setText("");
        }else {
            tvCode.setText(AppConfig.CODE);
        }

        tvLink.setText(AppConfig.apk_url);

        findViewById(R.id.btn_copy).setOnClickListener(v -> copy());
    }

    private void copy() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String copy = getResources().getString(R.string.app_name) + " " + AppConfig.apk_url + " 邀请码:" + tvCode.getText().toString();
        cm.setText(copy);
        ToastUtils.showShort("复制成功");
    }
}
