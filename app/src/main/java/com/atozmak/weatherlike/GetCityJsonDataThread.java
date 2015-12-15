package com.atozmak.weatherlike;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mak on 2015/10/12.
 */

/**
 * 上网获取json数据。
 */
public class GetCityJsonDataThread extends Thread {
    String city_id;
    Handler handler;
    int which;
    // final int msg_add_city = 1;
    // final int msg_update_city = 2;

    public GetCityJsonDataThread(String city_id, Handler handler, int which) {
        this.city_id = city_id;
        this.handler = handler;
        this.which = which;
    }

//----------------------分隔符----------------------

    private String json;
    //    private String city_name_from_net;
//    private String last_update;
//    private String chinese_text;
//    private String temperature_now;
    private List<String> now_ = null;
    //  public static Map<String, String> now_map = new HashMap<>();
    public static Map<String, String> day_map = null;
    public static Map<String, Map<String, String>> days_map = new HashMap<>();
    private String path_now;
    private String path_future;


//----------------------分隔符----------------------

    /**
     * 这是一个线程类，所以所有事情都在run()里面完成
     * 因为请求【即时天气数据】和【未来天气数据】的地址不同，
     * 而且，不同地址执行的解析行为也不同，所以分成2个次执行请求。
     */
    @Override
    public void run() {
        path_now = "https://api.thinkpage.cn/v2/weather/now.json?city=" +
                city_id +
                "&language=zh-chs&unit=c&key=GQBQAX5PSR";
        path_future = "https://api.thinkpage.cn/v2/weather/future.json?city=" +
                city_id +
                "&language=zh-chs&unit=c&key=GQBQAX5PSR";

        requestDataFromInternet(path_now);
        requestDataFromInternet(path_future);

        //Log.v("makdebug", "GetCityJsonDataThread   ");
        switch (which) {
            case 1:
                handler.sendMessage(handler.obtainMessage(1));
                break;
            case 2:
                handler.sendMessage(handler.obtainMessage(2));
                break;
        }
    }

//----------------------分隔符----------------------

    private void requestDataFromInternet(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                /**
                 * 将【InputStream】转换输出成【数组】。
                 */
                byte[] data = readStream(is);
                json = new String(data);

                // Log.v("makdebug", "GetCityJsonDataThread--json--" + json);

                if (path.equals(path_now)) {
                    parseNowJson(json);
                } else if (path.equals(path_future)) {
                    parseFutureJson(json);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//----------------------分隔符----------------------

    /**
     * 解析网络传回来的 即时json数据
     * 放进now_map里面
     * 再放进list里面
     *
     * @param json json数据
     */
    private void parseNowJson(String json) {
        //  Log.v("makdebug", "GetCityJsonDataThread--parseNowJson--" + json);

        try {
            JSONObject jsonObject_root = new JSONObject(json);
            JSONArray jsonArray_weather = jsonObject_root.getJSONArray("weather");
            JSONObject jsonObject_only = jsonArray_weather.getJSONObject(0);

            String city_id_from_net = jsonObject_only.getString("city_id");
            String city_name_from_net = jsonObject_only.getString("city_name");
            String last_update = jsonObject_only.getString("last_update");
            JSONObject jsonObject_now = jsonObject_only.getJSONObject("now");
            //  String chinese_text = jsonObject_now.getString("text");
            String temperature_now = jsonObject_now.getString("temperature");

            //   Log.v("makdebug", "GetCityJsonDataThread--city_name_from_net--" + city_name_from_net);

            /**
             * 遇到问题：
             * 数据库里面的future数据是正确的，但是now的数据出错了了，
             * 都显示为北京。
             * 注：position都为0.
             * ----------------------------------------------
             *问：为什么now_map里面竟然是有北京和澳门的数据，
             *      而不仅仅是两个澳门的数据。
             * 答：因为每次传数据进来的时候都new一个HashMap，
             *      而不是在全局声明就new HashMap<>()。
             * ----------------------------------------------
             * 总结：每次传数据进来的时候都new一个HashMap，然后加入list
             *      可以使得list里面不同position的数据是不同的。
             */
            Map<String, String> now_map = new HashMap<>();
            now_map.put("city_id_from_net", city_id_from_net);

            now_map.put("city_name_from_net", city_name_from_net);
            //  now_map.put("chinese_text", chinese_text);
            now_map.put("temperature_now", temperature_now);

            //    Log.v("makdebug", "GetCityJsonDataThread--- city_name_from_net  " + city_name_from_net+chinese_text);

            /**
             * 只需考虑单次的数据传入即可，
             * 因为fragment会从database取数据来显示。
             */
            MainActivity.now_list.add(now_map);

            // Log.v("makdebug", "GetCityJsonDataThread--now_map--" + now_map);

            //  Log.v("makdebug", "GetCityJsonDataThread--now_list--" + MainActivity.now_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//----------------------分隔符----------------------

    /**
     * 解析网络传回来的 未来天气情况json数据
     *
     * @param json json数据
     */
    private void parseFutureJson(String json) {
        try {
            JSONObject jsonObject_root = new JSONObject(json);
            JSONArray jsonArray_weather = jsonObject_root.getJSONArray("weather");
            JSONObject jsonObject_only = jsonArray_weather.getJSONObject(0);
            JSONArray day = jsonObject_only.getJSONArray("future");

            day_map = new HashMap<>();
            int len = 0;
            for (len = 0; len < day.length(); len++) {
                JSONObject jsonObject_one_day_all = day.getJSONObject(len);
                String day_week = jsonObject_one_day_all.getString("day");
                //  String day_text = jsonObject_one_day_all.getString("text");
                String day_high = jsonObject_one_day_all.getString("high");
                String day_low = jsonObject_one_day_all.getString("low");

                String lenString = len + "";

                day_map.put("day_week_" + lenString, day_week);
                // day_map.put("day_text_"+lenString, day_text);
                day_map.put("day_high_" + lenString, day_high);
                day_map.put("day_low_" + lenString, day_low);
            }
            MainActivity.day_list.add(day_map);

            //  Log.v("makdebug", "GetCityJsonDataThread--day_list--" + MainActivity.day_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//----------------------分隔符----------------------

    private byte[] readStream(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        /**
         * 【is.read(buffer)】从输入流中读取一定数量的字节，
         * 并将其存储在缓冲区数组buffer中。
         * len=读入缓冲区的总字节数。
         * is没close的话，is还是那个is，所以不断循环的话，会接着之前的指针读下去。
         */
        while ((len = is.read(buffer)) != -1)
        /**
         * 把数据写进输出流。
         */
            byteArrayOutputStream.write(buffer, 0, len);
        byteArrayOutputStream.close();
        is.close();
        return byteArrayOutputStream.toByteArray();
    }


}
