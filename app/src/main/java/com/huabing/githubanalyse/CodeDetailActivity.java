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

public class CodeDetailActivity extends AppCompatActivity {

    private WebView wvDetail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        overridePendingTransition(R.anim.activity_open,0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_detail);
        //项目名
        String program=getIntent().getStringExtra("program");
        //标题
        Toolbar toolbar=(Toolbar)findViewById(R.id.tb_toolbar);
        toolbar.setTitle(program);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        //初始化ProgressDialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(program);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        String address="https://github.com/"+program;

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
        wvDetail.loadUrl(address);
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
