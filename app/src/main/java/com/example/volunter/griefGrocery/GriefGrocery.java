package com.example.volunter.griefGrocery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.volunter.MainActivity.MyFragmentPagerAdapter;
import com.example.volunter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/5/28.
 */

public class GriefGrocery extends Fragment {

    private View parentView;
    private TabLayout tabLayout;
    private ViewPager viewPager;;
    private List<String> titleList;
    private List<Fragment> fragmentList;

    public GriefGrocery(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.grief_grocery, container, false);

        initView();
        initValue();

        return parentView;
    }

    private void initView(){
        tabLayout = (TabLayout) parentView.findViewById(R.id.tabLayout_griefGrocery);
        viewPager = (ViewPager) parentView.findViewById(R.id.viewPager_griefGrocery);
    }

    private void initValue(){
        fragmentList = new ArrayList<>();
        fragmentList.add(new LostAndFoundFragment());
        fragmentList.add(new BillboardFragment());
        titleList = new ArrayList<>();
        titleList.add("失物招领");
        titleList.add("“帮帮”告示牌");
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this.getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
