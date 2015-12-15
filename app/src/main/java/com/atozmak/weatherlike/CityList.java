package com.atozmak.weatherlike;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CityList extends AppCompatActivity implements View.OnClickListener {

    private final static String filename = "city_list_json.txt";
    private List<String> data_chinaGlobe = new ArrayList<>();
    private List<String> data_province = new ArrayList<>();
    private List<String> data_city = new ArrayList<>();
    private List<String> data_district = new ArrayList<>();
    private ListView listView_china_globe;
    private ListView listView_province;
    private ListView listView_city;
    private ListView listView_district;
    private int position_first = 0;
    private int position_second = 0;
    private int position_third = 0;
    private int position_forth = -1;
    ArrayAdapter<String> arrayAdapter_china;
    ArrayAdapter<String> arrayAdapter_province;
    ArrayAdapter<String> arrayAdapter_city = null;
    ArrayAdapter<String> arrayAdapter_district = null;

    private Intent intent;

    private String city_id;
    private Button choose_city_finish;

    //------------------------分隔符-----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
 //       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.main_orange);

        // getActionBar().setDisplayHomeAsUpEnabled(true);

        initFindViewById();
        new GetDataThread().start();

        choose_city_finish.setOnClickListener(this);
    }

    //------------------------分隔符-----------------------------

    private void initFindViewById() {
        listView_china_globe = (ListView) findViewById(R.id.country);
        listView_province = (ListView) findViewById(R.id.province);
        listView_city = (ListView) findViewById(R.id.city);
        listView_district = (ListView) findViewById(R.id.district);
        choose_city_finish = (Button) findViewById(R.id.city_choose_finish);
    }

    //------------------------分隔符-----------------------------

    /**
     * 把获得的json数据流放进map里面以便使用
     * String jsonStr = getCityListJson(filename);
     *
     * @param str 为jsonStr
     *            <p/>
     *            一开始的时候执行这一段
     */
    private void convertToAdapterData_entry(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            int len = jsonArray.length();
            for (int position_first = 0; position_first < len; position_first++) {
                JSONObject jsonObject = jsonArray.getJSONObject(position_first);
                data_chinaGlobe.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当点击第一列的item的时候，执行这一段
     *
     * @param str 为json数据
     */
    private void convertToAdapterData_first_column_item_click(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonObject = jsonArray.getJSONObject(position_first);
            JSONArray jsonArray_province = jsonObject.getJSONArray("list");
            int len2 = jsonArray_province.length();
            for (int j = 0; j < len2; j++) {
                JSONObject jsonObject_province = jsonArray_province.getJSONObject(j);
                data_province.add(jsonObject_province.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当点击第二列的item的时候，执行这一段
     *
     * @param str 为json数据
     */
    private void convertToAdapterData_second_column_item_click(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonObject = jsonArray.getJSONObject(position_first);
            JSONArray jsonArray_province = jsonObject.getJSONArray("list");

            JSONObject jsonObject_province = jsonArray_province.getJSONObject(position_second);
            JSONArray jsonArray_city = jsonObject_province.getJSONArray("list");
            int len3 = jsonArray_city.length();
            for (int j = 0; j < len3; j++) {
                JSONObject jsonObject_city = jsonArray_city.getJSONObject(j);
                data_city.add(jsonObject_city.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当点击第三列的item的时候，执行这一段
     *
     * @param str 为json数据
     */
    private void convertToAdapterData_third_column_item_click(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonObject = jsonArray.getJSONObject(position_first);
            JSONArray jsonArray_province = jsonObject.getJSONArray("list");

            JSONObject jsonObject_province = jsonArray_province.getJSONObject(position_second);
            JSONArray jsonArray_city = jsonObject_province.getJSONArray("list");

            JSONObject jsonObject_city = jsonArray_city.getJSONObject(position_third);
            JSONArray jsonArray_district = jsonObject_city.getJSONArray("list");

            int len3 = jsonArray_district.length();
            for (int j = 0; j < len3; j++) {
                JSONObject jsonObject_district = jsonArray_district.getJSONObject(j);
                data_district.add(jsonObject_district.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当点击第四列的item的时候，执行这一段
     * <p/>
     * 把id装进intent，然后返回给MainActivity
     *
     * @param str 为json数据
     */
    private void convertToAdapterData_forth_column_item_click(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonObject = jsonArray.getJSONObject(position_first);
            JSONArray jsonArray_province = jsonObject.getJSONArray("list");

            JSONObject jsonObject_province = jsonArray_province.getJSONObject(position_second);
            JSONArray jsonArray_city = jsonObject_province.getJSONArray("list");

            JSONObject jsonObject_city = jsonArray_city.getJSONObject(position_third);
            JSONArray jsonArray_district = jsonObject_city.getJSONArray("list");

            JSONObject jsonObject_district = jsonArray_district.getJSONObject(position_forth);
            /**
             * String city_id;
             */
            city_id = jsonObject_district.getString("id");

            Log.v("makdebug", "CityList.java convertToAdapterData_forth_column_item_click  " + city_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //------------------------分隔符-----------------------------

    /**
     * 读取本地文件assets中的Json数据：city_list_json城市列表。
     *
     * @param filename 文件名
     * @return 返回所有字符串
     */
    private String getCityListJson(String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = getAssets();
        Log.v("makdebug", "getAssets");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(filename)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //------------------------分隔符-----------------------------

    /**
     * 点击完成按钮之后返回数据给MainActivity
     * <p/>
     * position_forth=-1,当第四列listView有item被点击的话，
     * position_forth自动变为大于等于0的数，
     * 逻辑为：只有选中了具体的某个地区，点击“完成”按钮才有效
     *
     * @param v “完成”按钮
     */
    @Override
    public void onClick(View v) {
        GuideUtil.setBoolean(this, GuideUtil.SHOW_GUIDE, true);
        if (position_forth >= 0) {
            intent = getIntent();
            intent.putExtra("城市id", city_id);

            Log.v("makdebug", "CityList.java  city_id " + city_id);

            int resultCodeToMainActivity = 2;
            CityList.this.setResult(resultCodeToMainActivity, intent);
            //overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
            CityList.this.finish();
        } else {
            Toast.makeText(this, R.string.choose_city, Toast.LENGTH_SHORT).show();
        }
    }

    //------------------------分隔符-----------------------------

    /**
     * 建新线程来执行 获取json文件的输入流
     * <p/>
     * 当点击item的时候，先删除以前的数据，再更新数据，最后通知adapter更改数据
     */
    private class GetDataThread extends Thread {
        @Override
        public void run() {
            /**
             * 获得asset里面的城市json数据，并转化成String数据。
             */
            final String jsonStr = getCityListJson(filename);
            /**
             * 把要放进第一个ListView的数据，放进list里面
             */
            convertToAdapterData_entry(jsonStr);
            listView_china_globe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        data_province.clear();
                        data_city.clear();
                        data_district.clear();
                        position_first = position;
                        convertToAdapterData_first_column_item_click(jsonStr);
                        arrayAdapter_province.notifyDataSetChanged();
                    }else {
                        /**
                         * API不是会员，显示不了外国的天气。
                         */
                        Toast.makeText(CityList.this,"暂时不支持查询外国天气",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            listView_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    data_city.clear();
                    data_district.clear();
                    position_second = position;
                    convertToAdapterData_second_column_item_click(jsonStr);
                    arrayAdapter_city.notifyDataSetChanged();
                }
            });
            listView_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    data_district.clear();
                    position_third = position;
                    convertToAdapterData_third_column_item_click(jsonStr);
                    arrayAdapter_district.notifyDataSetChanged();
                }
            });
            listView_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position_forth = position;
                    /**
                     * 获得该地区的id。
                     */
                    convertToAdapterData_forth_column_item_click(jsonStr);
                }
            });
            dataHandler.sendMessage(dataHandler.obtainMessage());
        }
    }

    //------------------------分隔符-----------------------------

    class MyHandler extends Handler {
        WeakReference<CityList> cityListWeakReference;

        MyHandler(CityList cityList) {
            cityListWeakReference = new WeakReference<>(cityList);
        }

        @Override
        public void handleMessage(Message msg) {
            /**
             * 设置adapter来显示ListView的内容。
             *
             * 在子线程找到数据之后，在主线程将数据显示出来。
             */
            arrayAdapter_china = new ArrayAdapter<>(CityList.this, R.layout.city_list_content_form, data_chinaGlobe);
            listView_china_globe.setAdapter(arrayAdapter_china);

            arrayAdapter_province = new ArrayAdapter<>(CityList.this, R.layout.city_list_content_form, data_province);
            listView_province.setAdapter(arrayAdapter_province);

            arrayAdapter_city = new ArrayAdapter<>(CityList.this, R.layout.city_list_content_form, data_city);
            listView_city.setAdapter(arrayAdapter_city);

            arrayAdapter_district = new ArrayAdapter<>(CityList.this, R.layout.city_list_content_form, data_district);
            listView_district.setAdapter(arrayAdapter_district);
        }
    }

    MyHandler dataHandler = new MyHandler(this);

    //------------------------分隔符-----------------------------

    /**
     * //按下【返回键】的话，就会回到【主页】(会使得viewpager显示不出来,未知原因，迟D再查明)
     * 所以现在暂时不使用这个方法
     *
     * @param keyCode keyCode
     * @param event   event
     * @return return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "请点击【左上角】的【返回键】返回", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //------------------------分隔符-----------------------------
}

