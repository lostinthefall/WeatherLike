package com.atozmak.weatherlike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mak on 2015/11/28.
 */
public class AstroFragment extends Fragment {

    TextView tv_sign;
    TextView tv_total;
    TextView tv_job;
    TextView tv_love;
    TextView tv_money;
    TextView tv_today_is;

    TextView tv_choose_astro;

    private MyTask myTask;
    private String json;
    String date_today;
    //private AstroData astroData;

    private List<Map<String, String>> astro_list;
    private Map<String, String> astro_map;

    private Context context;

    private String[] astros_chn = new String[]{"白羊座", "金牛座", "双子座", "巨蟹座", "狮子" +
            "座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};
    private String[] astros_jap = new String[]{"牡羊座", "牡牛座", "双子座", "蟹座", "獅子座",
            "乙女座", "天秤座", "蠍座", "射手座", "山羊座", "水瓶座", "魚座"};
    /*牡羊座, =牡牛座, 双子座, n=蟹座, jobgn=獅子座, jn=乙女座,
                                           天秤座, jn=蠍座, j=射手座, jo=山羊座, 水瓶座, j, sign=魚座, j4}]*/

    private String select_astros = "白羊座";

    boolean isShow = false;
    private int astro;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.astro, container, false);

        /**
         * 第一次进入软件时
         */
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        astro_list = new ArrayList<>();
        initFindViewById(view);
        date_today = getDate();
        myTask = new MyTask();
        myTask.execute(date_today);


        /**
         * 需要选择自己的星座时
         */
        tv_choose_astro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("makdebug", "AlertDialog.Builder test1");
                AlertDialog.Builder builder = new AlertDialog.Builder(AstroFragment.this.getActivity())
                        .setTitle("我的星座")
                        .setSingleChoiceItems(astros_chn, 0, new DialogInterface.OnClickListener() {
                                    /**
                                     * 点击了某个星座之后就会联网获取数据，显示
                                     * @param dialog dialog
                                     * @param  which which
                                     */
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int len = astro_list.size();
                                        for (int i = 0; i < len; i++) {
                                            String sign = astro_list.get(i).get("sign");
                                            /**
                                             * 这里比较巧，
                                             * 本是打算把astros_chn的内容转换成jap的内容，
                                             * 结果面板显示成jap的内容，本以为会显示chn的内容
                                             * 然后就想到下面的方式了，就是突然看到才能想到的，
                                             * 这么巧的方法。
                                             */
                                            if (astros_jap[which].equals(sign)) {
                                                tv_sign.setText(astros_chn[which]);
                                                tv_total.setText(astro_list.get(i).get("total"));
                                                tv_job.setText(astro_list.get(i).get("job"));
                                                tv_love.setText(astro_list.get(i).get("love"));
                                                tv_money.setText(astro_list.get(i).get("money"));
                                                break;
                                            }
                                        }
                                        setAstro(which);
                                        /**
                                         * 点击某个选项后，dialog消失掉
                                         */
                                        dialog.dismiss();
                                    }
                                }
                        );
                builder.show();
                Log.v("makdebug", "AlertDialog.Builder test2");
            }
        });
        return view;
    }

    //----------------------------分隔符------------------------------

    public void setAstro(int astro) {

        editor.putInt("which_astro", astro);
        editor.commit();
    }

    public int getAstro() {
        int which_astro = sharedPref.getInt("which_astro", 2);
        Log.v("makdebug", "which_astro--" + which_astro);
        return which_astro;
    }


    //----------------------------分隔符------------------------------

    class MyTask extends AsyncTask {

        /**
         * 此方法用于在执行后台任务前做一些UI操作
         */
        @Override
        protected void onPreExecute() {
            tv_today_is.setText("更新中...");
        }

        /**
         * 执行后台任务，不能进行修改UI
         *
         * @param params 不知
         * @return 不知
         */
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL url = new URL("http://api.jugemkey.jp/api/horoscope/free/" + params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    byte[] data = readStream(is);
                    json = new String(data);
                    parseJson(json);
                }
                //Thread.sleep(1000);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 执行完后台任务后更新UI，显示结果
         *
         * @param o 不造
         */
        @Override
        protected void onPostExecute(Object o) {
            tv_today_is.setText(date_today + " 5星Max哟~");
            tv_today_is.setTextSize(16);
            isShow = GuideUtil.getBoolean(AstroFragment.this.getActivity(), GuideUtil.SHOW_GUIDE);
            if (isShow) {
                /**
                 * 不是首次进入的话,从保存的数据获取【星座名】，
                 * 然后去更新数据，显示
                 */
                int i = getAstro();

                tv_sign.setText(astros_chn[i]);

                //  tv_sign.setText(astro_list.get(i).get("sign"));
                tv_total.setText(astro_list.get(i).get("total"));
                tv_job.setText(astro_list.get(i).get("job"));
                tv_love.setText(astro_list.get(i).get("love"));
                tv_money.setText(astro_list.get(i).get("money"));
            } else {
                /**
                 * 首次进入软件，将会显示杜羊座的数据
                 */
                int i = 0;
                setAstro(i);
                tv_sign.setText(astro_list.get(i).get("sign"));
                tv_total.setText(astro_list.get(i).get("total"));
                tv_job.setText(astro_list.get(i).get("job"));
                tv_love.setText(astro_list.get(i).get("love"));
                tv_money.setText(astro_list.get(i).get("money"));
                Log.v("makdebug", "astro_list.get(i).get(\"sign\")" + astro_list.get(i).get("sign"));
            }
        }

        private void parseJson(String json) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<HoroscopeJsonBeanTwo>>() {
                }.getType();
                JSONObject root = new JSONObject(json);
                JSONObject horos = root.getJSONObject("horoscope");
                ArrayList<HoroscopeJsonBeanTwo> nums = gson.fromJson(horos.getString(date_today), type);
                int len = nums.size();
                for (int i = 0; i < len; i++) {
                    /**
                     * 把下载下来的json数据解析后放进AstroData类里面,这个用不了
                     * 换回用map来存
                     */
                    HoroscopeJsonBeanTwo two = nums.get(i);
                    astro_map = new HashMap<>();
                    astro_map.put("sign", two.getSign());
                    astro_map.put("total", two.getTotal());
                    astro_map.put("job", two.getJob());
                    astro_map.put("love", two.getLove());
                    astro_map.put("money", two.getMoney());

                    astro_list.add(astro_map);
                    //  Log.v("makdebug", "astro_lsit " + astro_list);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private byte[] readStream(InputStream is) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            return baos.toByteArray();
        }
    }

    //-------------------------分隔符------------------------------

    private void initFindViewById(View view) {
        tv_today_is = (TextView) view.findViewById(R.id.today_is);
        tv_sign = (TextView) view.findViewById(R.id.which_astro);
        tv_total = (TextView) view.findViewById(R.id.zongheyun_x_xing);
        tv_job = (TextView) view.findViewById(R.id.shiyeyun_x_xing);
        tv_love = (TextView) view.findViewById(R.id.aiqing_x_xing);
        tv_money = (TextView) view.findViewById(R.id.caifuyun_x_xing);
        tv_choose_astro = (TextView) view.findViewById(R.id.choose_astro);
    }

    //-------------------------分隔符------------------------------

    /**
     * 获取今天的日期
     */
    public String getDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(d);
        Log.v("makdebug", "date是哪个----" + date);
        return date;
    }

//-------------------------分隔符------------------------------

//-------------------------分隔符------------------------------
//-------------------------分隔符------------------------------
//-------------------------分隔符------------------------------
//-------------------------分隔符------------------------------
//-------------------------分隔符------------------------------
//-------------------------分隔符------------------------------
//-------------------------分隔符------------------------------

}
