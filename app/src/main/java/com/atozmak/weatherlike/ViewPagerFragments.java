package com.atozmak.weatherlike;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Mak on 2015/10/13.
 */
public class ViewPagerFragments extends Fragment {
    /**
     * 不能用构造器传参数，要用setArguments();
     */
    private TextView city_name;
    private TextView temperature_now;
    private TextView weather_status;
    private TextView temperature_day1_up;
    private TextView temperature_day1_down;
    private TextView temperature_day1_week;
    private TextView temperature_day1_date;
    private TextView temperature_day2_up;
    private TextView temperature_day2_down;
    private TextView temperature_day2_week;
    private TextView temperature_day2_date;
    private TextView temperature_day3_up;
    private TextView temperature_day3_down;
    private TextView temperature_day3_week;
    private TextView temperature_day3_date;
    private TextView temperature_day4_up;
    private TextView temperature_day4_down;
    private TextView temperature_day4_week;
    private TextView temperature_day4_date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager, container, false);

        Bundle bundle_private = getArguments();
        String city_name_from_net = bundle_private.getString("city_name_from_net");
        String chinese_text = bundle_private.getString("chinese_text");
        String temperature_now_1 = bundle_private.getString("temperature_now");

        /**
         * day_week_0 显示周几
         */
        String day_week_0 = bundle_private.getString("day_week_0");
        String day_text_0 = bundle_private.getString("day_text_0");
        String day_high_0 = bundle_private.getString("day_high_0");
        String day_low_0 = bundle_private.getString("day_low_0");

        String day_week_1 = bundle_private.getString("day_week_1");
        String day_text_1 = bundle_private.getString("day_text_1");
        String day_high_1 = bundle_private.getString("day_high_1");
        String day_low_1 = bundle_private.getString("day_low_1");

        String day_week_2 = bundle_private.getString("day_week_2");
        String day_text_2 = bundle_private.getString("day_text_2");
        String day_high_2 = bundle_private.getString("day_high_2");
        String day_low_2 = bundle_private.getString("day_low_2");

        String day_week_3 = bundle_private.getString("day_week_3");
        String day_text_3 = bundle_private.getString("day_text_3");
        String day_high_3 = bundle_private.getString("day_high_3");
        String day_low_3 = bundle_private.getString("day_low_3");

        initFindViewById(view);

        /**
         * 好麻烦，为什么当时不直接
         * 【 city_name.setText(bundle_private.getString("city_name_from_net"));】
         */
        city_name.setText(city_name_from_net);
        temperature_now.setText(temperature_now_1);
        weather_status.setText(chinese_text);

        temperature_day1_week.setText(day_text_0);
        temperature_day1_date.setText("今天" + day_week_0);
        temperature_day1_up.setText(day_high_0);
        temperature_day1_down.setText(day_low_0);

        temperature_day2_week.setText(day_text_1);
        temperature_day2_date.setText(day_week_1);
        temperature_day2_up.setText(day_high_1);
        temperature_day2_down.setText(day_low_1);

        temperature_day3_week.setText(day_text_2);
        temperature_day3_date.setText(day_week_2);
        temperature_day3_up.setText(day_high_2);
        temperature_day3_down.setText(day_low_2);

        temperature_day4_week.setText(day_text_3);
        temperature_day4_date.setText(day_week_3);
        temperature_day4_up.setText(day_high_3);
        temperature_day4_down.setText(day_low_3);

        return view;
    }

    private void initFindViewById(View view) {
        //    view.setTag("city_id"); 不行，因为view就是viewpager
        city_name = (TextView) view.findViewById(R.id.city_name);
        temperature_now = (TextView) view.findViewById(R.id.temperature_now);
        weather_status = (TextView) view.findViewById(R.id.weather_status);
//        temperature_day1_up
//         temperature_day1_down
//         temperature_day1_week
//        temperature_day1_date
        temperature_day1_up = (TextView) view.findViewById(R.id.temperature_day1_up);
        temperature_day1_down = (TextView) view.findViewById(R.id.temperature_day1_down);
        /**
         * temperature_day1_week 是显示 天气text
         */
        temperature_day1_week = (TextView) view.findViewById(R.id.temperature_day1_week);
        /**
         *   temperature_day1_date是显示周几
         */
        temperature_day1_date = (TextView) view.findViewById(R.id.temperature_day1_date);


        temperature_day2_up = (TextView) view.findViewById(R.id.temperature_day2_up);
        temperature_day2_down = (TextView) view.findViewById(R.id.temperature_day2_down);
        temperature_day2_week = (TextView) view.findViewById(R.id.temperature_day2_week);
        temperature_day2_date = (TextView) view.findViewById(R.id.temperature_day2_date);

        temperature_day3_up = (TextView) view.findViewById(R.id.temperature_day3_up);
        temperature_day3_down = (TextView) view.findViewById(R.id.temperature_day3_down);
        temperature_day3_week = (TextView) view.findViewById(R.id.temperature_day3_week);
        temperature_day3_date = (TextView) view.findViewById(R.id.temperature_day3_date);

        temperature_day4_up = (TextView) view.findViewById(R.id.temperature_day4_up);
        temperature_day4_down = (TextView) view.findViewById(R.id.temperature_day4_down);
        temperature_day4_week = (TextView) view.findViewById(R.id.temperature_day4_week);
        temperature_day4_date = (TextView) view.findViewById(R.id.temperature_day4_date);


    }

}
