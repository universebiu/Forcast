package com.example.forcast.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    /*初始化数据库的信息内容*/
    public static SQLiteDatabase database;

    public static void initDB(Context context){
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();//获取数据库对象，database成为全局对象
        //去UniteApp初始化数据库
    }
    /*查找数据库当中城市列表*/
    public static List<String> queryAllCityName(){
        Cursor cursor = database.query("info",null,null,null,null,null,null);
        List<String>cityList = new ArrayList<>();
        while (cursor.moveToNext()){
            String city = cursor.getString(cursor.getColumnIndex("city"));//得到cursor对象中的列名"city"
            cityList.add(city);
        }
        return cityList;
        //转到MainActivity
    }

    /*根据城市名称替换城市信息*/
    public static int updateInfoByCity(String city,String content){
        //调用database的update方法
        ContentValues values = new ContentValues();//得到values对象
        values.put("content",content);
        return database.update("info",values,"city=?",new String[]{city});
    }

    /*新增一条城市记录*/
    public static long addCityInfo(String city,String content){
        ContentValues values = new ContentValues();
        values.put("city",city);
        values.put("content",content);
        return database.insert("info",null,values);
    }

    /*根据城市名查询数据库当中的内容*/
    public static String queryInfoByCity(String city){
        Cursor cursor = database.query("info",null,"city=?",new String[] {city},null,null,null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            return content;
        }
        return null;
    }

    /*存储城市天气信息要求最多存储3个城市的信息，一旦超过3个城市就不能存储了*/
    public static int getCityCount(){
        Cursor cursor = database.query("info",null,null,null,null,null,null);
        int count = cursor.getCount();
        return count;
    }

    /*查询数据库当中的全部信息*/
    public static List<DataBaseBean>quertAllInfo() {
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        List<DataBaseBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            DataBaseBean bean = new DataBaseBean(id, city, content);
            list.add(bean);
        }
        return list;
    }

    /*根据城市名称删除这个城市在数据库当中的数据*/
    public static int deleteInfoByCity(String city){
        return database.delete("info","city=?",new String[]{city});
    }

    /*删除表中的所有数据信息*/
    public static void deleteAlInfo(){
        String sql = "delete from info";
        database.execSQL(sql);
    }
}
