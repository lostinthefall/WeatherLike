package com.atozmak.weatherlike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mak on 2015/10/20.
 */

/**
 * 用【SQLiteDatabase】打开数据库麻烦，
 * 所以使用【SQLiteOpenHelper】的【getReadableDatabase】、【getWritableDatabase】来打开数据库。
 */
public class DataBaseHelperUtil extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    /**
     * DATABASE_NAME是数据库文件件的名字，为weather。
     * <p/>
     * weather_info是 数据库文件件“weather”其中一个表。
     */
    private static final String DATABASE_NAME = "weather";
    SQLiteDatabase db;

    List<Map<String, String>> now_list_adapter = new ArrayList<>();
    List<Map<String, String>> future_list_adapter = new ArrayList<>();
    Map<String, String> now_map = null;
    Map<String, String> future_map = null;
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

    //-----------------------分隔符-----------------------

    public DataBaseHelperUtil(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //-----------------------分隔符-----------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists weather_info(_id integer primary key autoincrement," +
                "now_city_name char(255)," +
                "now_city_id char(255)," +
                "now_temp char(255)," +
                "now_text char(255)," +
                "future_day1_high char(255)," +
                "future_day1_low char(255)," +
                "future_day1_text char(255)," +
                "future_day1_day char(255)," +
                "future_day2_high char(255)," +
                "future_day2_low char(255)," +
                "future_day2_text char(255)," +
                "future_day2_day char(255)," +
                "future_day3_high char(255)," +
                "future_day3_low char(255)," +
                "future_day3_text char(255)," +
                "future_day3_day char(255)," +
                "future_day4_high char(255)," +
                "future_day4_low char(255)," +
                "future_day4_text char(255)," +
                "future_day4_day char(255)" +
                ")");
    }

    //-----------------------分隔符-----------------------

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //-----------------------分隔符-----------------------

    /**
     * 把数据放进database里面
     *
     * @param now_list now_list里面的的数据
     * @param day_list day_list里面的数据
     * @param position 哪个城市的数据
     */
    public void addWeatherData(List<Map<String, String>> now_list, List<Map<String, String>> day_list, int position) {

        db = this.getWritableDatabase();
        //   Log.v("makdebug", "helperUtil 1  " + now_list);

        /**
         * 如果database里面有该城市了，那就不添加了，就更新数据好了,
         */
        final int int_city_name = 1;
        boolean pass = false;
        Cursor cursor = db.query("weather_info", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String city_name = cursor.getString(int_city_name);
            pass = now_list.get(position).get("city_name_from_net").equals(city_name);
            if (pass) {
                /**
                 * 只更新数据
                 * 【这里没有写，下个版本再写吧】
                 */
                break;
            }
        }
        /**
         * 把数据从两个List里面拿出来
         * city_id_from_net
         * 取出【now】的数据。
         */
        String city_name_from_net = now_list.get(position).get("city_name_from_net");
        String city_id_from_net = now_list.get(position).get("city_id_from_net");
        String chinese_text = now_list.get(position).get("chinese_text");
        String temperature_now = now_list.get(position).get("temperature_now");

        //  Log.v("makdebug", "helperUtil 2  " + city_name_from_net);

        /**
         *               day_week_0 显示周几
         *               取出【未来几天预报】的数据。
         */
        String day_week_0 = day_list.get(position).get("day_week_0");
        String day_text_0 = day_list.get(position).get("day_text_0");
        String day_high_0 = day_list.get(position).get("day_high_0");
        String day_low_0 = day_list.get(position).get("day_low_0");

        String day_week_1 = day_list.get(position).get("day_week_1");
        String day_text_1 = day_list.get(position).get("day_text_1");
        String day_high_1 = day_list.get(position).get("day_high_1");
        String day_low_1 = day_list.get(position).get("day_low_1");

        String day_week_2 = day_list.get(position).get("day_week_2");
        String day_text_2 = day_list.get(position).get("day_text_2");
        String day_high_2 = day_list.get(position).get("day_high_2");
        String day_low_2 = day_list.get(position).get("day_low_2");

        String day_week_3 = day_list.get(position).get("day_week_3");
        String day_text_3 = day_list.get(position).get("day_text_3");
        String day_high_3 = day_list.get(position).get("day_high_3");
        String day_low_3 = day_list.get(position).get("day_low_3");

        //  Log.v("makdebug", "helperUtil 3  " + day_week_0);

        /**
         * 把从List拿出来的数据放进DataBase里面。
         */
        ContentValues values = new ContentValues();

        values.put("now_city_id", city_id_from_net);
        values.put("now_city_name", city_name_from_net);
        values.put("now_text", chinese_text);
        values.put("now_temp", temperature_now);

        values.put("future_day1_high", day_high_0);
        values.put("future_day1_low", day_low_0);
        values.put("future_day1_text", day_text_0);
        values.put("future_day1_day", day_week_0);

        values.put("future_day2_high", day_high_1);
        values.put("future_day2_low", day_low_1);
        values.put("future_day2_text", day_text_1);
        values.put("future_day2_day", day_week_1);

        values.put("future_day3_high", day_high_2);
        values.put("future_day3_low", day_low_2);
        values.put("future_day3_text", day_text_2);
        values.put("future_day3_day", day_week_2);

        values.put("future_day4_high", day_high_3);
        values.put("future_day4_low", day_low_3);
        values.put("future_day4_text", day_text_3);
        values.put("future_day4_day", day_week_3);

        db.insert("weather_info", null, values);
        db.close();

    }

    //-----------------------分隔符-----------------------

    public int getCountFromDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from weather_info", null);
        int num = cursor.getCount();
        //  Log.v("makdebug", "getCountFromDatabase  count(*) " + num);
        db.close();
        return num;
    }

    //-----------------------分隔符-----------------------

    public void setFragmentArguments(int position) {
        Cursor cursor;
        /**
         * 不再从List里面获取数据，从DataBase里面获取数据。
         *
         * 把数据从database里面拿出来放到list和map里面供提取。
         */
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query("weather_info", null, null, null, null, null, null);
        if (cursor != null) {
            /**
             * 移动到指定行
             */
            //db.delete("weather_info", "now_city_name='澳门'", null);
            //  db.delete("weather_info", "_id=33", null);
            cursor.moveToPosition(position);
            //  Log.v("makdebug", "DataBaseHelperUtil setFragmentArguments moveToPosition  " + position);

            /**
             * 把数据从database中取出来
             */
            String now_city_name = cursor.getString(int_now_city_name);
            String now_temp = cursor.getString(int_now_temp);
            String now_text = cursor.getString(int_now_text);

            now_map = new HashMap<>();
            now_map.put("now_temp", now_temp);
            now_map.put("now_text", now_text);
            now_map.put("now_city_name", now_city_name);
            now_list_adapter.add(now_map);

            //Log.v("makdebug", "DataBaseHelperUtil--setFragmentArguments--now_city_name--" + now_city_name);

            String future_day1_high = cursor.getString(int_future_day1_high);
            String future_day1_low = cursor.getString(int_future_day1_low);
            String future_day1_text = cursor.getString(int_future_day1_text);
            String future_day1_day = cursor.getString(int_future_day1_day);

            String future_day2_high = cursor.getString(int_future_day2_high);
            String future_day2_low = cursor.getString(int_future_day2_low);
            String future_day2_text = cursor.getString(int_future_day2_text);
            String future_day2_day = cursor.getString(int_future_day2_day);

            /**
             * 把从database取出来的数据放进list里面
             */
            future_map = new HashMap<>();
            future_map.put("future_day1_high", future_day1_high);
            future_map.put("future_day1_low", future_day1_low);
            future_map.put("future_day1_text", future_day1_text);
            future_map.put("future_day1_day", future_day1_day);

            future_map.put("future_day2_high", future_day2_high);
            future_map.put("future_day2_low", future_day2_low);
            future_map.put("future_day2_text", future_day2_text);
            future_map.put("future_day2_day", future_day2_day);
            future_list_adapter.add(future_map);
        }
        /**
         * 用完就关闭
         */
        db.close();

        Map<String, String> a = now_list_adapter.get(position);
        Map<String, String> b = future_list_adapter.get(position);

        ViewPagerFragments viewPagerFragments = new ViewPagerFragments();
        //  Log.v("makdebug", "b      " + b);
        /**
         * 把从database取出来放进list里面的数据取出来放进发给fragment的bundle里面。
         */
        Bundle bundle = new Bundle();
        /**
         * 多麻烦啊，
         * 直接【bundle.putString("city_name_from_net", cursor.getString(int_now_city_name))】不就好了;
         */
        bundle.putString("city_name_from_net", a.get("now_city_name"));
        bundle.putString("chinese_text", a.get("now_text"));
        bundle.putString("temperature_now", a.get("now_temp"));
        /**
         *                day_week_0 显示周几
         */
        bundle.putString("day_high_0", b.get("future_day1_high"));
        bundle.putString("day_low_0", b.get("future_day1_low"));
        bundle.putString("day_text_0", b.get("future_day1_text"));
        bundle.putString("day_week_0", b.get("future_day1_day"));

        bundle.putString("day_high_1", b.get("future_day2_high"));
        bundle.putString("day_low_1", b.get("future_day2_low"));
        bundle.putString("day_text_1", b.get("future_day2_text"));
        bundle.putString("day_week_1", b.get("future_day2_day"));

        viewPagerFragments.setArguments(bundle);
        MainActivity.listFragments.add(viewPagerFragments);
    }

    //-----------------------分隔符-----------------------

}
