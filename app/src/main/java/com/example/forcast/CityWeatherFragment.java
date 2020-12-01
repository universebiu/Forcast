package com.example.forcast;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.forcast.base.BaseFragment;
import com.example.forcast.bean.WeatherBean;
import com.example.forcast.db.DBManager;
import com.google.gson.Gson;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

//主页面显示城市天气信息
//通过提供修改API接口中的城市名字，使用联网函数向服务器发送请求，接收服务器响应，最后通过JSON数据解析，将解析得到数据绑定到相应的组件中
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {

    TextView tempTv,cityTv,conditionTv,windTv,tempRangTv,dateTv,clothIndexTv,carIndexTv,coldIndexIv,sportIndexTv,raysIndexTv;
    ImageView dayIv;
    LinearLayout futureLayout;
    ScrollView outlayout;
    //天气接口
    String url1 = "https://free-api.heweather.net/s6/weather/?location=";
    String url2 = "&key=201e8165fe894334b59fcdfe1b8f7bd2";
    List<WeatherBean.HeWeather6Bean.LifestyleBean> indexList;
    String weather[]={"晴","阴","雨","多云","大雨","暴雨","雷阵雨","阵雨","中雨"};
    String city;
    private SharedPreferences pref;
    private int bgNum;

    public CityWeatherFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        //可以通过activity传值获取当前fragment加载的是哪个地方的天气情况
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        Log.d("ASS", "onCreate:5");
        String url = url1+city+url2;
        //调取父类获取数据的方法
        loadData(url);
        return  view;
    }
    @Override
    public void onSuccess(String result){
        //解析数据
        parseShowData(result);
        //加载数据成功后更新数据
        int i = DBManager.updateInfoByCity(city,result);
        if(i<=0){
            //更新数据库失败，没有这个城市，增加这个城市记录
            DBManager.addCityInfo(city,result);
        }

    }
    @Override
    public void onError(Throwable ex,boolean isOnCallback){
        // 数据库当中查找上一次信息显示在Fragment中
        String s = DBManager.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)){
            parseShowData(s);
        }
    }

    //Json
    private void parseShowData(String result) {
        //使用Json解析数据
        WeatherBean weatherBean = new Gson().fromJson(result,WeatherBean.class);
        WeatherBean.HeWeather6Bean.BasicBean basicBean = weatherBean.getHeWeather6().get(0).getBasic();
        //获取指数信息存放在列表
        List<WeatherBean.HeWeather6Bean.LifestyleBean> indexLists = weatherBean.getHeWeather6().get(0).getLifestyle();
        indexList=indexLists;
        //当前天气情况
        WeatherBean.HeWeather6Bean.NowBean nowBean = weatherBean.getHeWeather6().get(0).getNow();
        //未来天气集合
        List<WeatherBean.HeWeather6Bean.DailyForecastBean> futureList = weatherBean.getHeWeather6().get(0).getDaily_forecast();
        //设置TextView
        dateTv.setText(futureList.get(0).getDate());//日期
        cityTv.setText(basicBean.getParent_city());//城市
        //设置当天的天气情况
        WeatherBean.HeWeather6Bean.DailyForecastBean todayDataBean = futureList.get(0);
        windTv.setText(nowBean.getWind_dir());//当前风向
        String str1 = todayDataBean.getTmp_min()+"℃~"+todayDataBean.getTmp_max()+"℃";//最高温和最低温
        tempRangTv.setText(str1);
        conditionTv.setText(nowBean.getCond_txt());//当前天气情况
        String str2 = nowBean.getTmp()+"℃";
        tempTv.setText(str2);
        //显示天气情况的照片。
        Log.d("Ass", "onCreate: 9");
        String s1 = nowBean.getCond_txt();
        if(s1.equals(weather[0])){
            dayIv.setImageResource(R.drawable.sun);//晴
        }
        else if(s1.equals(weather[1])){
            dayIv.setImageResource(R.drawable.yintian);//阴
        }
        else if(s1.equals(weather[2])){
            dayIv.setImageResource(R.drawable.yu);//小雨
        }
        else if(s1.equals(weather[3])){
            dayIv.setImageResource(R.drawable.duoyun);//多云
        }
        else if(s1.equals(weather[4])){
            dayIv.setImageResource(R.drawable.dayu);//大雨
        }
        else if(s1.equals(weather[5])){
            dayIv.setImageResource(R.drawable.baoyu);//暴雨
        }
        else if(s1.equals(weather[6])){
            dayIv.setImageResource(R.drawable.leizhenyu);//雷阵雨
        }
        else if(s1.equals(weather[7])){
            dayIv.setImageResource(R.drawable.zhenyu);//阵雨
        }
        else if(s1.equals(weather[8])){
            dayIv.setImageResource(R.drawable.zhongyu);//中雨
        }
        else {
            dayIv.setImageResource(R.drawable.jiazai);//加载未添加天气的图片
        }

        Log.d("Ass", "onCreate: 11");
        //获取未来三天的天气情况，加载到layout中
        for(int i=0;i<futureList.size();i++){
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center,null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemView);
            TextView idateTv = itemView.findViewById(R.id.item_center_tv_date);
            TextView iconTv  = itemView.findViewById(R.id.item_tv_con);
            TextView itemprangeTv = itemView.findViewById(R.id.item_center_tv_temp);
            //加载图片
            ImageView iIv = itemView.findViewById(R.id.item_center_iv);
            //获取对应位置的天气情况
            WeatherBean.HeWeather6Bean.DailyForecastBean dataBean = futureList.get(i);
            idateTv.setText(dataBean.getDate());
            if(i==0){
                iconTv.setText(nowBean.getCond_txt());
            }
            else {
                iconTv.setText(dataBean.getCond_txt_d());
            }
            String str3 = dataBean.getTmp_min()+"℃~"+dataBean.getTmp_max()+"℃";
            itemprangeTv.setText(str3);
            String s2;
            //更换图片
            if(i==0){
                s2=nowBean.getCond_txt();
            }
            else{s2 = dataBean.getCond_txt_d();}
            if(s2.equals(weather[0])){
                iIv.setImageResource(R.drawable.sun);
            }
            else if(s2.equals(weather[1])){
                iIv.setImageResource(R.drawable.yintian);
            }
            else if(s2.equals(weather[2])){
                iIv.setImageResource(R.drawable.yu);
            }
            else if(s2.equals(weather[3])){
                iIv.setImageResource(R.drawable.duoyun);
            }
            else if(s2.equals(weather[4])){
                iIv.setImageResource(R.drawable.dayu);
            }
            else if(s2.equals(weather[5])){
                iIv.setImageResource(R.drawable.baoyu);
            }
            else if(s2.equals(weather[6])){
                iIv.setImageResource(R.drawable.leizhenyu);
            }
            else if(s2.equals(weather[7])){
                iIv.setImageResource(R.drawable.zhenyu);
            }
            else if(s2.equals(weather[8])){
                iIv.setImageResource(R.drawable.zhongyu);
            }
            else {
                iIv.setImageResource(R.drawable.jiazai);
            }
        }

    }

    public void initView(View view){
        //用于初始化控件
        tempTv = view.findViewById(R.id.frag_tv_currenttemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangTv = view.findViewById(R.id.frag_tv_temprange);
        dateTv = view.findViewById(R.id.frag_tv_date);
        clothIndexTv = view.findViewById(R.id.frag_index_tv_dress);
        carIndexTv = view.findViewById(R.id.frag_index_tv_washcar);
        coldIndexIv = view.findViewById(R.id.frag_index_tv_cold);
        sportIndexTv = view.findViewById(R.id.frag_index_tv_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_tv_rays);
        dayIv = view.findViewById(R.id.frag_iv_today);
        futureLayout = view.findViewById(R.id.frag_center_layout);
        outlayout = view.findViewById(R.id.out_layout);

        //设置点击事件的监听
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexIv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);

    }

    public void onClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        List<WeatherBean.HeWeather6Bean.LifestyleBean> List  = indexList;
        switch (v.getId()){
            case  R.id.frag_index_tv_dress:
                builder.setTitle("穿衣指数");
                String msg =List.get(1).getBrf()+"\n"+ List.get(1).getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_washcar:
                builder.setTitle("洗车指数");
                msg =List.get(6).getBrf()+"\n"+ List.get(6).getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_cold:
                builder.setTitle("感冒指数");
                msg =List.get(2).getBrf()+"\n"+ List.get(2).getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_sport:
                builder.setTitle("运动指数");
                msg =List.get(3).getBrf()+"\n"+ List.get(3).getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_rays:
                builder.setTitle("紫外线指数");
                msg =List.get(5).getBrf()+"\n"+ List.get(5).getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
        }
        builder.create().show();
    }
}
