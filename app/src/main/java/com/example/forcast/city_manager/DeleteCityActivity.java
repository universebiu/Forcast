package com.example.forcast.city_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.forcast.R;
import com.example.forcast.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView errorIv,rightIv;
    ListView deleteLv;
    List<String>mDatas;//ListView原始的数据源
    List<String>deleteCitys;//存储了删除的城市信息
    private DeleteCityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        errorIv = findViewById(R.id.delete_iv_error);
        rightIv = findViewById(R.id.delete_iv_right);
        deleteLv = findViewById(R.id.delete_lv);
        mDatas = DBManager.queryAllCityName();    //获取数据库中有哪些城市
        deleteCitys = new ArrayList<>();
        //设置监听事件
        errorIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
        //适配器的设置
       adapter =  new DeleteCityAdapter(this,mDatas,deleteCitys);
       deleteLv.setAdapter(adapter);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_iv_error:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息").setMessage("您确定要放弃更改吗？")
                        .setPositiveButton("放弃更改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                break;
            case R.id.delete_iv_right:
                for (int i=0;i<deleteCitys.size();i++){
                    String city = deleteCitys.get(i);
                    //调用删除城市的函数
                    DBManager.deleteInfoByCity(city);
                }
                //删除成功后返回上一级页面
                finish();
                break;
        }
    }
}
