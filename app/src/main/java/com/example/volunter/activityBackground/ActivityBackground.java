package com.example.volunter.activityBackground;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.volunter.MainActivity.MyFragmentPagerAdapter;
import com.example.volunter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/5/28.
 */

public class ActivityBackground extends Fragment {

    private View parentView;
    private TabLayout tabLayout;
    private ViewPager viewPager;;
    private List<String> titleList;
    private List<Fragment> fragmentList;

    public ActivityBackground(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_background, container, false);
        initView();
        initValue();
        return parentView;
    }

    private void initView(){
        tabLayout = (TabLayout) parentView.findViewById(R.id.tabLayout_activityBackground);
        viewPager = (ViewPager) parentView.findViewById(R.id.viewPager_activityBackground);
    }

    private void initValue(){
        fragmentList = new ArrayList<>();
        fragmentList.add(new ActivityRecommendFragment());
        fragmentList.add(new HotspotActivityFragment());
        fragmentList.add(new LastestActivityFragment());
        titleList = new ArrayList<>();
        titleList.add("活动推荐");
        titleList.add("热点活动");
        titleList.add("最新活动");
        //解决多个viewpager下数据丢失问题
        FragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this.getChildFragmentManager(),
                fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
