package com.example.forcast.base;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import androidx.fragment.app.Fragment;
//回调接口
public class BaseFragment extends Fragment implements Callback.CommonCallback<String> {
    public void loadData(String path){
        //网络请求操作
        RequestParams params = new RequestParams(path);//请求网址的路径
        x.http().get(params,this);
    }
    //获取数据成功时，回调的接口
    @Override
    public void onSuccess(String result){
    }
    //获取数据失败时回调的接口
    @Override
    public void onError(Throwable ex,boolean isOnCallback){
        Log.d("Ass", "onError: errorddddddd");
    }
    //取消请求时回调的接口
    @Override
    public void onCancelled(CancelledException cex){
    }
    //请求完成时回调的接口
    @Override
    public void onFinished(){
    }
}
