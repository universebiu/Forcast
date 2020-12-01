package com.example.forcast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CityFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment>fragmentList;
    CityFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentLis) {
        super(fm);
        this.fragmentList = fragmentLis;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    int childCount  = 0;//表示viewpager包含地页数
    //当页数发生改变时必须重写的两个函数
    @Override
    public void notifyDataSetChanged(){
        this.childCount = childCount;
        super.notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(@NonNull  Object object){
        if (childCount>0){
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
