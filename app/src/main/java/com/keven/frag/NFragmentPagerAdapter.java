package com.keven.frag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;


public class NFragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;

    public NFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        Log.d("TAG", "********** NFragmentPagerAdapter="+ NFragmentPagerAdapter.this+"     fragment="+list.get(arg0));
        return list.get(arg0);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        Log.d("TAG", "********** NFragmentPagerAdapter="+ NFragmentPagerAdapter.this+"     Object="+o+"     position="+position+"     list="+list.size());
        list.set(position, (Fragment)o);
        return o;
    }
}