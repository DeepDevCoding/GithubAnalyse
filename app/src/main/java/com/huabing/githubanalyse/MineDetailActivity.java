package com.huabing.githubanalyse;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MineDetailActivity extends AppCompatActivity {

    private WebView wvDetail;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_detail);
        Toolbar toolbar=(Toolbar)findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //获取用户名
        String account=getIntent().getStringExtra("account");
        //设置标题栏
        toolbar.setTitle(account);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(account);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);

        String mineAddress="https://github.com/"+account;
        wvDetail=(WebView)findViewById(R.id.wv_detail);
        wvDetail.getSettings().setJavaScriptEnabled(true);
        wvDetail.setWebViewClient(new WebViewClient()
        {
            //加载前
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                progressDialog.show();
            }
            //加载后
            @Override
            public void onPageFinished(WebView view, String url)
            {
                progressDialog.dismiss();
            }
        });
        wvDetail.loadUrl(mineAddress);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
