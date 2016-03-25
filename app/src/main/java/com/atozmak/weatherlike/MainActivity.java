package com.atozmak.weatherlike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static List<Fragment> listFragments = new ArrayList<>();
    //  public static List<String> list_city = new ArrayList<>();

    public static ViewPager viewPager;
    private HorizontalScrollView hScrollView;
    private ViewGroup mTrack;
    public MyFragmentPagerAdapter myFragmentPagerAdapter = null;
    private ImageButton imageButton;
    private Intent intent;
    private String city_id;
    //   public static Bundle bundle = new Bundle();
    public static List<Map<String, String>> now_list = new ArrayList<>();
    public static List<Map<String, String>> day_list = new ArrayList<>();
    public static List<String> data_base_city_name_list = new ArrayList<>();
    //  public static Map<String, String> data_base_city_name_map = new HashMap<>();

    Context context;
    TextView child;
    boolean isShow = false;
    DataBaseHelperUtil dbUtil;
    String city_name_from_net;
    int int_id = 0;
    int id_vGetId;
    int id_vGetId_longclick;
    int click_times = -1;

    AstroFragment astroFragment;

    TanmuMainAcitivity tanmuMainAcitivity;

    String city_id_update;
    List<String> updateList;

    int click_times_update;

    int num_update_list;

    //  FrameLayout containerFL;
    //TextView textViewTanmu;

    //--------------------------分隔符--------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * 依赖了一个开源项目【systembartint】
         */
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        /**
         * 这里有点坑啊，一定是setResource才行，setColor也不行。
         */
        tintManager.setStatusBarTintResource(R.color.main_orange);

        //   Log.v("makdebug", "onCreate");

        isShow = GuideUtil.getBoolean(this, GuideUtil.SHOW_GUIDE);
        /**
         * 首次进入软件会显示城市列表，
         * isShow = false 的话，就显示城市列表。
         */
        if (isShow) {
            /**
             * 不是首次进入的话，
             * 从database里面提取数据并且显示。
             */
            initFindViewById();
            initImageButton();
            initGetDataFromSQLite();
            initIndicator();
            initFragment();

            astroFragment = new AstroFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_container, astroFragment).commit();
            //  Log.v("makdebug", "new asfragment");


        } else {
            click_times = 0;
            initGuide();

            initFindViewById();
            initImageButton();
            initGetDataFromSQLite();
            initIndicator();
            initFragment();
            // myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), now_list, dbUtil);

            astroFragment = new AstroFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_container, astroFragment).commit();
            //click_times = -1;
        }
    }

    //--------------------------分隔符--------------------------

    private void initGetDataFromSQLite() {

        dbUtil = new DataBaseHelperUtil(MainActivity.this);
        //  dbUtil.getReadableDatabase().delete("weather_info", null, null);
    }

    //--------------------------分隔符--------------------------

    private void initGuide() {
        intent = new Intent(MainActivity.this, CityList.class);
        int requestCodeToCityList = 1;
        startActivityForResult(intent, requestCodeToCityList);
    }

    //--------------------------分隔符--------------------------

    private void initIndicator() {

        SQLiteDatabase db = dbUtil.getReadableDatabase();

        Cursor cursor = db.query("weather_info", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            /**
             * city_name在数据库的位置为1
             */
            city_name_from_net = cursor.getString(1);
            /**
             * 【mTrack】的控件就是【HorizontalScrollView】中的【LinearLayout】。
             */
            child = (TextView) LayoutInflater.from(this).inflate(R.layout.radio_button, mTrack, false);
            child.setId(int_id);
            // Log.v("makdebug", "SingleClick  int_id  " + int_id);
            child.setText(city_name_from_net);
            //  data_base_city_name_map.put(int_id+"",city_name_from_net);
            /**
             * 【data_base_city_name_list】是一个【List】
             */
            data_base_city_name_list.add(city_name_from_net);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Log.v("makdebug", "SingleClick  child  " + String.valueOf(child));
                    id_vGetId = v.getId();
//                    Log.v("makdebug", "SingleClick  id_vGetId  " + id_vGetId);
//                    Log.v("makdebug", "SingleClick  listFragments  " + listFragments.size());
//                    Log.v("makdebug", "listFragments.size()---; " + listFragments.size());
                    viewPager.setCurrentItem(id_vGetId);
                    /**
                     * 在这里操作textView的背景颜色就没问题，
                     * 但在xml文件里用state来操作就改变不了背景色，
                     * 原因未明。
                     */
                }
            });
            child.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    id_vGetId_longclick = v.getId();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("你要删除该城市吗？");
                    setPositiveButton(builder);
                    setNegativeButton(builder)
                            .create()
                            .show();
                    return true;
                }
            });
            mTrack.addView(child);
            int_id++;
        }
        int_id = 0;
    }

    //--------------------------分隔符--------------------------

    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**
                 * do nothing
                 */
            }
        });
    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**
                 * 删除database中该城市的数据，
                 * 并且删除指示器中的显示
                 */

                SQLiteDatabase db = dbUtil.getReadableDatabase();
                /**
                 * 删除指定行的城市
                 */
                String city_name_from_net = data_base_city_name_list.get(id_vGetId_longclick);

                db.delete("weather_info", "now_city_name='" + city_name_from_net + "'", null);

                //   Log.v("makdebug", "LongClick--- " + city_name_from_net);
                /**
                 * 做不到一个一个的删除，只能全部删除之后从新加载了。
                 * 下个版本在弄吧。
                 */
                restartAll();
                /*viewPager.removeAllViews();
                mTrack.removeAllViews();
                listFragments.clear();
                myFragmentPagerAdapter.notifyDataSetChanged();
                initFindViewById();
                initImageButton();
                initGetDataFromSQLite();
                initIndicator();
                initFragment();*/
            }
        });
    }

    //--------------------------分隔符--------------------------

    /**
     * database里面有多少行就init多少个fragment
     */
    private void initFragment() {
        int num = dbUtil.getCountFromDatabase();
        //Log.v("makdebug", " initFragment()添加了新城市之后database里有多少行 " + num);

        for (int i = 0; i < num; i++) {
            dbUtil.setFragmentArguments(i);
        }
        myFragmentPagerAdapter
                = new MyFragmentPagerAdapter(context, getSupportFragmentManager(), dbUtil, listFragments);
        viewPager.setAdapter(myFragmentPagerAdapter);

        // Log.v("makdebug", "initFragment--listFragments.size--" + listFragments.size());

    }

    //--------------------------分隔符--------------------------

    /**
     * 初始化FindViewById
     */
    private void initFindViewById() {
        imageButton = (ImageButton) findViewById(R.id.add_city);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        hScrollView = (HorizontalScrollView) findViewById(R.id.hSrcollView);
        mTrack = (ViewGroup) findViewById(R.id.track);
        /**
         * 这里是弹幕部分，本打算把代码放在普通类，然后在MainActivity里面new一下就ok，殊不知良辰做不到啊~~
         * 水平不够。
         */
        //containerFL = (FrameLayout) findViewById(R.id.frame_container_tanmu);
        //  textViewTanmu = new TextView(this);

    }
    //--------------------------分隔符--------------------------

    /**
     * 点击增加城市的按钮，跳转到CityList.java的界面，显示城市列表让用户选择。
     */
    private void initImageButton() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_times++;
                intent = new Intent(MainActivity.this, CityList.class);
                int requestCodeToCityList = 1;
                startActivityForResult(intent, requestCodeToCityList);
                /**
                 * 【overridePendingTransition】方法是设置Activity进入和退出的动画的。
                 * 从Activity【A】跳转到Activity【B】，
                 * 【R.anim.zoom_in】是【B】的进入动画，【R.anim.zoom_out】是【A】的退出动画，
                 */
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
    }
    //--------------------------分隔符--------------------------

    /**
     * 当用户在CityList.java选择了城市之后，返回城市的id信息。
     * 在这里接受id信息，用来给服务器发送请求。
     *
     * @param requestCode int requestCodeToCityList =1;
     */
    GetCityJsonDataThread getCityJsonDataThread;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 下面Intent data不要写错了，不要写成Intent intent
         */
        city_id = data.getStringExtra("城市id");
        //    Log.v("makdebug", "onActivityResult  city_id  " + city_id);

        /**
         * 上网获取json数据。
         */
        getCityJsonDataThread = new GetCityJsonDataThread(city_id, handler, 1);
        getCityJsonDataThread.start();

    }

    //--------------------------分隔符--------------------------

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                Log.v("makdebug", "now_list--msg.what == 1--" + now_list);

                dbUtil.addWeatherData(now_list, day_list, click_times);
                restartAll();
                MainActivity.viewPager.setCurrentItem(listFragments.size() - 1);
            } else if (msg.what == 2) {
                /**
                 * 这样就避免了【更新之后】【添加新城市】的话会导致显示出错。
                 */
                if (!(click_times_update == (num_update_list - 1))) {
                    dbUtil.addWeatherData(now_list, day_list, click_times_update);
                    //   now_list.clear();
                    // day_list.clear();
                    Log.v("makdebug", "now_list--msg.what == 2--" + now_list);
                    restartAll();
                    click_times_update++;
                    //Log.v("makdebug", "click_times--msg.what == 2--" + click_times);
                } else {
                    dbUtil.addWeatherData(now_list, day_list, click_times_update);
                    restartAll();
                    now_list.clear();
                    day_list.clear();
                    Log.v("makdebug", "now_list--msg.what == 2--else--" + now_list);

                }
            }


           /*
            //initFragment();
            *//**
             * （放在子线程就不行，放在这里就行，也还是没懂。）
             *
             * 把数据放进数据库后，就要把数据从数据库里拿出来给viewpager的fragment
             *//*
            *//**
             * 从网络下载并解析完数据之后要做什么。
             *
             * X=0代表list中第几个数据，这里为简单操作，
             * 所以每次点击【+】按钮之后，如果想再增加城市的话，要退出应用，重新进入之后点【+】按钮
             * 真正的实现为：记录点击【+】按钮的次数记为y，X=y-1;
             *//*
            dbUtil.addWeatherData(now_list, day_list, click_times);
            *//**
             * 把新增的数据放进数据库之后，
             * 要把数据拿出来显示。
             *//*
            //  dbUtil.setFragmentArguments(listFragments.size());

            // myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), now_list, dbUtil);

            viewPager.removeAllViews();
            mTrack.removeAllViews();
            listFragments.clear();
            myFragmentPagerAdapter.notifyDataSetChanged();

            initFindViewById();
            initImageButton();
            initGetDataFromSQLite();
            initIndicator();
            initFragment();
            // Log.v("makdebug", "listFragments.size()---3; " + listFragments.size());
            //initResume();

            *//*SQLiteDatabase db = dbUtil.getReadableDatabase();
            Cursor cursor = db.query("weather_info", null, null, null, null, null, null);
            cursor.moveToLast();
            city_name_from_net = cursor.getString(1);
            child = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.radio_button, mTrack, false);
            child.setId(int_new + int_id);
            child.setText(city_name_from_net);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    viewPager.setCurrentItem(id);
                }
            });
            mTrack.addView(child);*//*


            *//**
             * 这里没有错，能到达想要到达的位置。
             *//*
            MainActivity.viewPager.setCurrentItem(listFragments.size() - 1);

            // initIndicator();
            */
        }
    };


    //--------------------------分隔符--------------------------

    /**
     * 没有显示menu出来是因为这两个方法刚才都没有写出来啊：
     * 【onCreateOptionsMenu】【onOptionsItemSelected】
     *
     * @param menu menu
     * @return return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                /**
                 * 点击刷新按钮之后---1.隐藏按钮。2.显示【更新中...】
                 * 如果隐藏按钮的话会引发【空指针异常】，所以弃用【1.】了。
                 * 弹幕显示更新
                 */
                // new TanmuMainAcitivity(context,containerFL,textViewTanmu).startTanmu();
                //  startTanmu();
                tanmuMainAcitivity = new TanmuMainAcitivity();
                getSupportFragmentManager().beginTransaction().add(R.id.frame_tanmu, tanmuMainAcitivity).commit();
                updateAllDate();
                break;
            case R.id.action_introduction:
                Intent intent = new Intent(MainActivity.this, Introduce_container.class);
                startActivity(intent);


                break;
        }

        return true;
    }

    //--------------------------分隔符--------------------------

    /**
     * 查询【数据库】里面有什么城市，
     * 取出id，
     * 把id放进一个list里面，
     * 删除database里面的数据，
     * 然后用【addWeatherData方法】就ok了。
     */
    private void updateAllDate() {
        now_list.clear();
        day_list.clear();
        SQLiteDatabase db = dbUtil.getReadableDatabase();
        int num = dbUtil.getCountFromDatabase();
        final int int_new_city_id = 2;
        Cursor cursor = db.query("weather_info", null, null, null, null, null, null);
        updateList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            if (cursor != null) {
                cursor.moveToPosition(i);
                city_id_update = cursor.getString(int_new_city_id);
                // Log.v("makdebug", "city_id_update---" + city_id_update);
                updateList.add(city_id_update);
            }
        }
        db.delete("weather_info", null, null);
        num_update_list = updateList.size();
        /**
         * 显示错误的原因是这个【 click_times_update】
         */
        click_times_update = 0;

        for (int i = 0; i < num_update_list; i++) {
            String city_id_in_the_list = updateList.get(i);
            Log.v("makdebug", "city_id_in_the_list---" + city_id_in_the_list);
            // click_times++;
            getCityJsonDataThread = new GetCityJsonDataThread(city_id_in_the_list, handler, 2);
            getCityJsonDataThread.start();
            //  SystemClock.sleep(1000);
            Log.v("makdebug", "updateAllDate---now_list---" + now_list);

        }
        //            dbUtil.addWeatherData(now_list, day_list, click_times);
        //  restartAll();
        cursor.close();

        /**
         * 星座数据的更新。
         * 最简单的方法，哈哈哈，把AstroFragment删了，然后重新加载。
         */
        getSupportFragmentManager().beginTransaction().remove(astroFragment).commit();
        Fragment updateAstroFragment = new AstroFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, updateAstroFragment).commit();

    }

    //--------------------------分隔符--------------------------

    private void restartAll() {
        viewPager.removeAllViews();
        mTrack.removeAllViews();
        listFragments.clear();
        myFragmentPagerAdapter.notifyDataSetChanged();

        initFindViewById();
        initImageButton();
        initGetDataFromSQLite();
        initIndicator();
        initFragment();
        /*
        *       viewPager.removeAllViews();
                mTrack.removeAllViews();
                listFragments.clear();
                myFragmentPagerAdapter.notifyDataSetChanged();
                initFindViewById();
                initImageButton();
                initGetDataFromSQLite();
                initIndicator();
                initFragment();
        *
        * */
    }


    //--------------------------分隔符--------------------------

}


