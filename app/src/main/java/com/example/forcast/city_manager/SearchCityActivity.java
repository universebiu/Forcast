package com.example.forcast.city_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forcast.MainActivity;
import com.example.forcast.R;
import com.example.forcast.base.BaseActivity;
import com.example.forcast.bean.WeatherBean;
import com.google.gson.Gson;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener{
    EditText searchEt;
    ImageView submitIv,backIv;
    GridView searchGv;
    String [] hotCitys = {"北京","广州","上海","深圳","佛山","珠海","澳门","苏州","厦门",
            "长沙","成都","南京","杭州","武汉","重庆"};//热门城市
    private ArrayAdapter<String>adapter;//创建设配器
    String city;
    String url1 = "https://free-api.heweather.net/s6/weather/?location=";
    String url2 = "&key=201e8165fe894334b59fcdfe1b8f7bd2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        backIv = findViewById(R.id.search_city_back);
        searchEt = findViewById(R.id.search_et);            //获取搜索框对象
        submitIv = findViewById(R.id.search_iv_submit);     //获取搜索键
        searchGv = findViewById(R.id.search_gv);            //获取网格视图
        submitIv.setOnClickListener(this);                 //搜索键监听
        backIv.setOnClickListener(this);                   //返回键监听

        //创建数组适配器对象，并且通过参数设置类item_hotcity布局样式和数据源
        adapter = new ArrayAdapter<>(this,R.layout.item_hotcity,hotCitys);
        searchGv.setAdapter(adapter);//把数组适配器加载到searchGv网格视图控件中
        setListener();

    }
/*设置监听事件*/
    private void setListener() {
        searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = hotCitys[position];      //获取被点击的热门城市
                String url = url1+city+url2;    //通过点击的热门城市生成API接口
                loadData(url);                  //通过API接口搜索该城市信息
            }
        });
    }
/*更多设置单击事件监听*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_city_back:    //返回键
                finish();               //返回上一层
                break;
            case R.id.search_iv_submit://
                city = searchEt.getText().toString();
                if(!TextUtils.isEmpty(city)){
                    //判断能否找到这个城市
                    String url = url1+city+url2;
                    loadData(url);//通过API接口搜索该城市信息
                }else{
                    Toast.makeText(this, "输入内容不能为空！", Toast.LENGTH_SHORT).show(); //Text弹窗
                }
                break;
        }
    }
    @Override
    /*在监听者实现监听器接口后的回调方法*/
    public void onSuccess(String result) {
        WeatherBean weatherBean = new Gson().fromJson(result,WeatherBean.class);
        String status = "ok";
        if(weatherBean.getHeWeather6().get(0).getStatus().equals(status)){  //判断城市是否存在
            Intent intent  = new Intent(this, MainActivity.class);  //获取主页对象
            //返回所获得的城市信息
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("city",city);
            startActivity(intent);
        }else{
            Toast.makeText(this, "暂未收到此城市天气信息", Toast.LENGTH_SHORT).show();
        }
    }
}
