package com.atozmak.weatherlike;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by Mak on 2015/12/4.
 */
public class AnimationHelper {
    public static Animation creatTranslateAnim(Context context, int fromX, int toX) {
        TranslateAnimation tla = new TranslateAnimation(fromX, toX, 0, 0);
        tla.setDuration(4000);
        tla.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                /**
                 * 之前是【 return 0;】
                 * 原来是这里导致不显示啊！！！！！
                 * 【return 0 】就是就是返回【0】的速度啊！！
                 * 怪不得不显示了！！！！
                 */
                return input;
            }
        });
        tla.setFillAfter(true);
        return tla;
    }
}
