package com.atozmak.weatherlike;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mak on 2015/10/20.
 */
public class GuideUtil {
    /**
     * 是否显示一开始的城市选择界面，true表示显示，false表示不显示
     */
    public static final String SHOW_GUIDE = "showGuide";

    /**
     * 第一次选择完城市之后，把值设置为false。
     */
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public  static boolean getBoolean(Context context, String key){
        SharedPreferences sharedPreferences= context.getSharedPreferences("preference", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
}
