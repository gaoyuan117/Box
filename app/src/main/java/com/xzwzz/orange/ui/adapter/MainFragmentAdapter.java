package com.xzwzz.orange.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * @Project_Name :xiaoniao
 * @package_Name :com.vlive.aiqiji.xzwzz
 * @AUTHOR :xzwzz@vip.QqBean.com
 * @DATE :2018/3/23  14:03
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private FragmentManager mFragmentManager;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment o = (Fragment) super.instantiateItem(container, position);
        mFragmentManager.beginTransaction().show(o).commit();
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        mFragmentManager.beginTransaction().hide(mFragments.get(position)).commit();
    }
}
