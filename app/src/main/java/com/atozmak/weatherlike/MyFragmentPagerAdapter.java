package com.atozmak.weatherlike;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mak on 2015/10/10.
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    List<Map<String, String>> now_list;
    DataBaseHelperUtil dataBaseHelperUtil;
    final int int_now_city_name = 1;
    final int int_now_temp = 3;
    final int int_now_text = 4;
    final int int_future_day1_high = 5;
    final int int_future_day1_low = 6;
    final int int_future_day1_text = 7;
    final int int_future_day1_day = 8;
    final int int_future_day2_high = 9;
    final int int_future_day2_low = 10;
    final int int_future_day2_text = 11;
    final int int_future_day2_day = 12;

    List<Map<String, String>> now_list_adapter = new ArrayList<>();
    List<Map<String, String>> future_list_adapter = new ArrayList<>();
    Map<String, String> now_map = null;
    Map<String, String> future_map = null;
    Cursor cursor;
    List<Fragment> listFragments;

    private Context context;

    public MyFragmentPagerAdapter(Context context, FragmentManager fm,
                                  DataBaseHelperUtil dataBaseHelperUtil, List<Fragment> listFragment) {
        super(fm);
        this.context = context;
        this.now_list = now_list;
        this.dataBaseHelperUtil = dataBaseHelperUtil;
        this.listFragments = listFragment;
        //  Log.v("makdebug", "MyFragmentPagerAdapter  ");
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {

        //  Log.v("makdebug", " MyFragmentPagerAdapter  getItem  position  "+position);

        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        //   Log.v("makdebug", " MyFragmentPagerAdapter getCount");
        return listFragments.size();
    }

    /**
     * 可以使得完全清除viewpager中的fragment
     *
     * @param object null
     * @return null
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
