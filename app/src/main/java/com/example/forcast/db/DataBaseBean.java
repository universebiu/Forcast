package com.example.forcast.db;

//设置表信息
public class DataBaseBean {
    private int _id;//城市id
    private String city;//城市名字
    private String content;//城市天气信息

    public DataBaseBean(int _id, String city, String content) {
        this._id = _id;
        this.city = city;
        this.content = content;
    }
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
