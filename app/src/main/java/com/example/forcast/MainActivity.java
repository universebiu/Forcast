package com.example.forcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.forcast.city_manager.CityManagerActivity;
import com.example.forcast.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView addCityIv,moreIv;
    LinearLayout pointLayout;
    RelativeLayout outlayout;
    ViewPager mainVp;
    List<Fragment>fragmentList;     //ViewPager的数据源
    List<String>cityList;       //表示选中的城市的集合
    List<ImageView> imgList;    //表示ViewPager的页数指示器显示的内容
    private CityFragmentPagerAdapter adapter;
    private SharedPreferences pref;
    private int bgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv =findViewById(R.id.main_iv_add);
        moreIv=findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        outlayout = findViewById(R.id.main_out_layout);
        //exchangeBg();
        mainVp = findViewById(R.id.main_vp);

        Log.d("ASS", "onCreate:1 ");

        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
        Log.d("ASS", "onCreate:2 ");

        fragmentList = new ArrayList<>();
        cityList = new ArrayList<>();
        imgList = new ArrayList<>();
        //       城市列表
        cityList = DBManager.queryAllCityName();//获取数据库包含的城市信息列表
        if(cityList.size()==0){
            cityList.add("广州");
        }
        /*获取搜索界面传来的城市*/
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        if(!cityList.contains(city)&&!TextUtils.isEmpty(city)){
            cityList.add(city);
        }

        //初始化ViewPager
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainVp.setAdapter(adapter);
        //创建小圆点指示器
        initPoint();
        //设置最后一个城市表示的信息
        mainVp.setCurrentItem(fragmentList.size()-1);
        //设置ViewPager页面监听
        setPagerListener();
    }

    private void setPagerListener() {
        /*设置页面变换时的监听事件*/
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            //换小圆点
            public void onPageSelected(int position) {
                    for (int i=0;i<imgList.size();i++){
                        imgList.get(i).setImageResource(R.mipmap.heidian);
                    }
                    imgList.get(position).setImageResource(R.mipmap.baidian);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initPoint() {
        //创建小圆点，ViewPager页面指示器的函数
        for(int i=0;i<fragmentList.size();i++){
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.heidian);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(pIv);
            pointLayout.addView(pIv);
        }
        imgList.get(imgList.size()-1).setImageResource(R.mipmap.baidian);
    }

    private void initPager(){
//        创建fragment对象添加到viewPager数据源当中
        for(int i=0;i<cityList.size();i++){
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    //添加点击事件
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_iv_add:
                Log.d("Ass", "onCreate: 33");
                intent.setClass(this, CityManagerActivity.class);
                Log.d("Ass", "onCreate: 34");
                break;
            case R.id.main_iv_more:
                intent.setClass(this,CityManagerActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    /*当Activity页面重新加载时会调用的函数,这个函数在页面获取焦点之前进行调用，在此处完成ViewPager页数的更新*/
    public void onRestart(){
        super.onRestart();
        //获取当前数据库还剩下的城市集合
        List<String> List = DBManager.queryAllCityName();
        if(List.size() == 0){
            List.add("广州");
        }
        cityList.clear();//重新加载之前清空原来的数据源
        cityList.addAll(List);
        //剩余的城市也要创建对应的fragment页面
        fragmentList.clear();
        initPager();

        adapter.notifyDataSetChanged();

        //页面数量发生改变，指示器的数量也会发生变化，重写设置添加指示器
        imgList.clear();
        pointLayout.removeAllViews();
        initPoint();
        mainVp.setCurrentItem(fragmentList.size()-1);
    }
}
