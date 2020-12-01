package com.example.forcast.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.forcast.R;
import com.example.forcast.bean.WeatherBean;
import com.example.forcast.db.DataBaseBean;
import com.google.gson.Gson;

import java.util.List;

public class CityManagerAdapter extends BaseAdapter {
    Context context;
    List<DataBaseBean>mDatas;//数据源是数据库中的每一条数据

    public CityManagerAdapter(Context context,List<DataBaseBean>mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();//返回数据的长度
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);//返回指定位置对应的元素
    }

    @Override
    public long getItemId(int position) {
        return position;//返回对应的位置
    }

    @Override
    /*对每一个item的内容进行初始化*/
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){//判断有没有可以复用的view
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_manager,null);
            //为空说明内部city 没有找到
            holder = new ViewHolder(convertView);//传入控件所在的convertView
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        //得到数据设置到指定的textView当中
        DataBaseBean bean = mDatas.get(position);
        holder.cityTv.setText(bean.getCity());
        //Json
        WeatherBean weatherBean =  new Gson().fromJson(bean.getContent(), WeatherBean.class);
        //获取当前天气情况
        WeatherBean.HeWeather6Bean.DailyForecastBean dataBean = weatherBean.getHeWeather6().get(0).getDaily_forecast().get(0);
        WeatherBean.HeWeather6Bean.NowBean nowBean = weatherBean.getHeWeather6().get(0).getNow();
        String ss1="天气："+nowBean.getCond_txt();
        holder.conTv.setText(ss1);
        holder.windTv.setText(nowBean.getWind_dir());
        String ss2 = dataBean.getTmp_min()+"℃~"+dataBean.getTmp_max()+"℃";
        holder.tempRangeTv.setText(ss2);
        String ss3 = nowBean.getTmp()+"℃";
        holder.currentTempTv.setText(ss3);
        return convertView;
    }
    /*关于ListView优化的一部分*/
    class ViewHolder{
        TextView cityTv,conTv,windTv,tempRangeTv,currentTempTv;
        public ViewHolder(View itemView){
            currentTempTv = itemView.findViewById(R.id.item_city_tv_temp);
            cityTv = itemView.findViewById(R.id.item_city_tv_city);
            conTv = itemView.findViewById(R.id.item_city_tv_condition);
            windTv = itemView.findViewById(R.id.item_city_tv_wind);
            tempRangeTv = itemView.findViewById(R.id.item_city_tv_temprange);
        }
    }
}
