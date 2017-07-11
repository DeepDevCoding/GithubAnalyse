package com.huabing.githubanalyse;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout rlVersion;
    private TextView tvState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar tbCheckVision=(Toolbar)findViewById(R.id.tb_check_vision);
        tbCheckVision.setTitle("设置");
        setSupportActionBar(tbCheckVision);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        rlVersion=(RelativeLayout)findViewById(R.id.rl_version);
        rlVersion.setOnClickListener(this);
        tvState=(TextView)findViewById(R.id.tv_state);
        tvState.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rl_version:
                String state="    当前版本为1.0.0\n    最新版本请关注https://github.com/shuiliuxing或" +
                        "联系作者1270129994@qq.com\n\n    备注：本人为大三学生,该软件只为开源学习使用，不涉及任何商业利益！";
                tvState.setText(state);
                break;
            default:
                break;
        }
    }

}
