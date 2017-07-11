package com.huabing.githubanalyse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.huabing.githubanalyse.Adapter.MainPagerAdapter;
import com.huabing.githubanalyse.Bean.Mine;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout dlDrawer;
    private NavigationView mainNavigation;
    private CircleImageView civUser;
    private TextView tvAccount;
    private TextView tvTime;
    private TextView tvLogin;
    private ViewPager vpPager;
    private List<Fragment> fragmentList;
    private FragmentManager manager;
    private MainPagerAdapter adapter;
    private BottomNavigationBar bnbNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DrawerLayout布局
        dlDrawer=(DrawerLayout)findViewById(R.id.dl_drawer);
        //标题栏
        Toolbar toolbar=(Toolbar)findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.tooolbar_home);
        }

        //ViewPager初始化
        vpPager=(ViewPager)findViewById(R.id.vpPager);
        //4个Fragment
        fragmentList=new ArrayList<>();
        fragmentList.add(CodeFragment.newInstance());
        fragmentList.add(UserFragment.newInstance());
        fragmentList.add(HotFragment.newInstance());
        fragmentList.add(NewsFragment.newInstance());
        //FragmentManager
        manager=getSupportFragmentManager();
        //FragmentPagerAdapter
        adapter=new MainPagerAdapter(manager,fragmentList);
        vpPager.setAdapter(adapter);
        //ViewPager选事件
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bnbNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //底部导航栏
        bnbNavigationBar=(BottomNavigationBar)findViewById(R.id.bnb_navigation);
        bnbNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bnbNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bnbNavigationBar.addItem(new BottomNavigationItem(R.drawable.main_bottom_hot,"热门")
                                .setInActiveColor(R.color.colorGray)
                                .setActiveColorResource(R.color.colorGreenDeep))
                        .addItem(new BottomNavigationItem(R.drawable.main_bottom_user,"用户")
                                .setInActiveColor(R.color.colorGray)
                                .setActiveColorResource(R.color.colorGreenDeep))
                        .addItem(new BottomNavigationItem(R.drawable.main_bottom_sort,"语言")
                                .setInActiveColor(R.color.colorGray)
                                .setActiveColorResource(R.color.colorGreenDeep))
                        .addItem(new BottomNavigationItem(R.drawable.main_bottom_news,"日报")
                                .setInActiveColor(R.color.colorGray)
                                .setActiveColorResource(R.color.colorGreenDeep))
                        .initialise();
        //底部导航栏事件
        bnbNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                vpPager.setCurrentItem(position);
            }
            @Override
            public void onTabUnselected(int position) {

            }
            @Override
            public void onTabReselected(int position) {

            }
        });
        //默认当前选中
        bnbNavigationBar.selectTab(0);

        //NavigationView
        mainNavigation=(NavigationView)findViewById(R.id.main_navigation);
        mainNavigation.setCheckedItem(R.id.menu_it_mine);
        mainNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                switch (item.getItemId())
                {
                    //我的Github
                    case R.id.menu_it_mine:
                        if(tvLogin.getText().equals("已登录"))
                        {
                            List<Mine> mineList = DataSupport.findAll(Mine.class);
                            if (mineList.size()>0)
                            {
                                String account = mineList.get(0).getAccount();
                                Intent intent = new Intent(MainActivity.this, MineDetailActivity.class);
                                intent.putExtra("account", account);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"请先登录！",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //收藏
                    case R.id.menu_it_collect:
                        Intent collectIntent=new Intent(MainActivity.this,CollectionActivity.class);
                        startActivity(collectIntent);
                        break;
                    //更换皮肤
                    case R.id.menu_it_skin:
                        Intent skinIntent=new Intent(MainActivity.this,SkinActivity.class);
                        startActivity(skinIntent);
                        break;
                    //设置
                    case R.id.menu_it_setting:
                        Intent settingIntent=new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(settingIntent);
                        break;
                    //退出
                    case R.id.menu_it_exit:
                        finish();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //登录github
        View navHeaderView=mainNavigation.getHeaderView(0);;
        civUser=(CircleImageView)navHeaderView.findViewById(R.id.navi_civ_image);
        tvAccount=(TextView)navHeaderView.findViewById(R.id.navi_tv_account);
        tvTime=(TextView)navHeaderView.findViewById(R.id.navi_tv_time);
        tvLogin=(TextView)navHeaderView.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(this);
        //初始数据
        List<Mine> mineList=DataSupport.findAll(Mine.class);
        if(mineList.size()>0)
        {
            String password=mineList.get(0).getPassword();
            if(password!=null)
            {
                tvAccount.setText(mineList.get(0).getAccount());
                String createTime=mineList.get(0).getCreateTime();
                createTime=createTime.replaceAll("[a-zA-Z]"," ");
                tvTime.setText(createTime);
                tvLogin.setText("已登录");
                Glide.with(this).load(mineList.get(0).getPicUrl()).into(civUser);
            }
        }
    }

    //弹出菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);
        return true;
    }

    //弹出菜单的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                dlDrawer.openDrawer(GravityCompat.START);
                break;
            case R.id.tb_search:
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
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
            case R.id.tv_login:
                Intent intent=new Intent(this,LoginActivity.class);
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
    }

    //下一个Activity返回数据处理
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode)
        {
            case 1:
                if(resultCode==RESULT_OK)
                {
                    //获取数据
                    String account=data.getStringExtra("account");
                    String password=data.getStringExtra("password");
                    String picUrl=data.getStringExtra("picUrl");
                    String programUrl=data.getStringExtra("programUrl");
                    String blog=data.getStringExtra("blog");
                    String createTime=data.getStringExtra("createTime");
                    createTime=createTime.replaceAll("[a-zA-Z]"," ");
                    //更新UI
                    Glide.with(this).load(picUrl).into(civUser);
                    tvAccount.setText(account);
                    tvTime.setText(createTime);
                    tvLogin.setText("已登录");
                }
                break;
            default:
                break;
        }
    }

}
