package com.example.forcast.city_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.forcast.R;
import com.example.forcast.db.DBManager;
import com.example.forcast.db.DataBaseBean;

import java.util.ArrayList;
import java.util.List;

/*城市管理的信息展示*/
public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView addIv,backIv,deleteIv,refresh;
    ListView cityLv;
    List<DataBaseBean> mdatas;//显示列表数据源
    private CityManagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addIv = findViewById(R.id.city_iv_add);
        backIv =findViewById(R.id.city_iv_back);
        deleteIv = findViewById(R.id.city_iv_delete);
        cityLv = findViewById(R.id.city_lv);
        refresh = findViewById(R.id.button_refresh);
        mdatas = new ArrayList<>();
        //添加监听事件
        addIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        refresh.setOnClickListener(this);
        //设置适配器
        adapter = new CityManagerAdapter(this,mdatas);
        cityLv.setAdapter(adapter);
    }
    /*删除后重新获取焦点，获取数据库当中真实数据源添加到原有数据源当中，提示适配器更新*/
    @Override
    protected void onResume(){
        super.onResume();
        List<DataBaseBean> list = DBManager.quertAllInfo();
        mdatas.clear();
        mdatas.addAll(list);
        adapter.notifyDataSetChanged();//提示适配器更新
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.city_iv_add:
                int cityCount = DBManager.getCityCount();
                if(cityCount<3) {
                    Intent intent = new Intent(this, SearchCityActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "存储城市数量已达上限，请删除后再添加", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_delete:
                Intent intent1 = new Intent(this,DeleteCityActivity.class);
                startActivity(intent1);
                break;
            case R.id.button_refresh:
                List<DataBaseBean> list = DBManager.quertAllInfo();
                mdatas.clear();
                mdatas.addAll(list);
                adapter.notifyDataSetChanged();
                Toast.makeText(CityManagerActivity.this,"强制刷新成功！", Toast.LENGTH_LONG).show();
                break;
        }
    }

}
