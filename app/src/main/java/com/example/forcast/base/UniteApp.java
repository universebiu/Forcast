package com.example.forcast.base;

import android.app.Application;

import com.example.forcast.db.DBManager;

import org.xutils.x;

//数据库创建函数
public class UniteApp extends Application {

    public void onCreate(){
        super.onCreate();
        x.Ext.init(this);
        DBManager.initDB(this);//一旦项目工程创建，数据库也被创建
    }
}
