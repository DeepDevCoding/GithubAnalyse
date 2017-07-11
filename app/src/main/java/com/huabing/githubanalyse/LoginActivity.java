package com.huabing.githubanalyse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.huabing.githubanalyse.Bean.Mine;
import com.huabing.githubanalyse.Bean.RenderScriptFastBlur;
import com.huabing.githubanalyse.Util.BitmapUtil;
import com.huabing.githubanalyse.Util.HttpUtil;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView civUser;
    private EditText etAccount;
    private EditText etPassword;
    private CheckBox cbRemember;
    private Button btnLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21)
        {
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        //背景虚化
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.ll_layout);
        Bitmap initBitmap = BitmapUtil.drawableToBitmap(getResources().getDrawable(R.drawable.login_background));//拿到初始图
        Bitmap blurBitmap = RenderScriptFastBlur.blurBitmap(this, initBitmap, 24f); //处理得到模糊效果的图
        Drawable drawable = new BitmapDrawable(blurBitmap);
        linearLayout.setBackground(drawable);

        //圆形用户头像
        civUser=(CircleImageView)findViewById(R.id.civ_user);

        //账号密码输入框
        etAccount=(EditText)findViewById(R.id.et_account);
        etPassword=(EditText)findViewById(R.id.et_password);

        //记住密码
        cbRemember=(CheckBox)findViewById(R.id.cb_remember);
        //登录按钮
        btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_login:
                progressDialog.show();
                //Log.e("账号：",etAccount.getText().toString());
                String address="https://api.github.com/users/"+etAccount.getText();
                HttpUtil.sendOkHttpRequest(address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        String jsonData=response.body().string();
                        try
                        {
                            JSONObject jsonObject=new JSONObject(jsonData);
                            final String picUrl=jsonObject.getString("avatar_url");
                            final String programUrl=jsonObject.getString("repos_url");
                            final String blog=jsonObject.getString("blog");
                            final String createTime=jsonObject.getString("created_at");
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    String account=etAccount.getText().toString();
                                    String password=etPassword.getText().toString();
                                    Intent intent=new Intent();
                                    intent.putExtra("account",account);
                                    intent.putExtra("password",password);
                                    intent.putExtra("picUrl",picUrl);
                                    intent.putExtra("programUrl",programUrl);
                                    intent.putExtra("blog",blog);
                                    intent.putExtra("createTime",createTime);
                                    setResult(RESULT_OK,intent);
                                    //是否记住密码
                                    if(!cbRemember.isChecked())
                                        password=null;
                                    //存入数据库
                                    List<Mine> mineList = DataSupport.findAll(Mine.class);
                                    if (mineList.size() > 0)
                                    {
                                        Mine mine = new Mine();
                                        mine.setAccount(account);
                                        mine.setPassword(password);
                                        mine.setPicUrl(picUrl);
                                        mine.setProgramUrl(programUrl);
                                        mine.setBlog(blog);
                                        mine.setCreateTime(createTime);
                                        mine.updateAll("id = ?", "1");
                                    }
                                    else
                                    {
                                        Mine mine = new Mine();
                                        mine.setAccount(account);
                                        mine.setPassword(password);
                                        mine.setPicUrl(picUrl);
                                        mine.setProgramUrl(programUrl);
                                        mine.setBlog(blog);
                                        mine.setCreateTime(createTime);
                                        mine.save();
                                    }
                                    progressDialog.dismiss();
                                    finish();
                                }
                            });
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }


}
