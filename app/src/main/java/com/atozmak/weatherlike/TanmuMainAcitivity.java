package com.atozmak.weatherlike;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mak on 2015/12/4.
 */
public class TanmuMainAcitivity extends Fragment {


    private TanmuHandler handler;
    private TanmuBean tanmuBean;
    private RelativeLayout containerRL;
    private int validHeightSpace;
    private Set<Integer> existMarginValues;
    //  private TextView textViewTanmu;
    private int linesCount;

    //TextView textViewTanmu;

    //Activity context;
    //--------------------------分隔符--------------------------


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tanmu_holder_relativelayout, container, false);

        containerRL = (RelativeLayout) view.findViewById(R.id.tanmu_holder_relative);
        //textViewTanmu = new TextView(getActivity());

        //  textViewTanmu = new TextView();
        existMarginValues = new HashSet<>();
        handler = new TanmuHandler(this);
        tanmuBean = new TanmuBean();
        tanmuBean.setItems(new String[]{"更新中...", "更新中啦 []~(￣▽￣)~*", "别烦我(￣ε(#￣)！",
                "更新一次容易吗我",
                "6666666666(￣３￣)a", "快更快更~~~~", "错过了又要等一年啊", "前面的等等我", "坚决反对泡面番！",
                "这并不是泡尿番", "好了，快更好了",
                "别骗我啊∑( ° △ °|||)︴", "差不多了~~~", "好啦！ (＞д＜) ","更完啦┐（—__—）┌"});
        //  Log.v("makdebug", "tanmu--2-");
        tanmuBean.setColors(new String[]{"#227700","#179741",
                "#8C0044","#4400CC","#9900FF","#770077","#BB5500","dc0e03"});

        if (containerRL.getChildCount() > 1) {

        }
        //  Log.v("makdebug", "tanmu--3-");

        existMarginValues.clear();
        new Thread(new CreatTanmuThread()).start();
        //   Log.v("makdebug", "tanmu--4-");

        return view;
    }

    //--------------------------分隔符--------------------------

    static class TanmuHandler extends Handler {
        private WeakReference<TanmuMainAcitivity> ref;

        TanmuHandler(TanmuMainAcitivity tma) {
            ref = new WeakReference<>(tma);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                TanmuMainAcitivity tma = ref.get();
                if (tma != null && tma.tanmuBean != null) {
                    int index = msg.arg1;
                    String content = tma.tanmuBean.getItems()[index];
                    float textSize = (float) (tma.tanmuBean.getMinTextSize()
                            * (1 +Math.random() * tma.tanmuBean.getRange()));
                    //int textColor = tma.tanmuBean.getColor();
                    //int textColor = Integer.parseInt(tma.tanmuBean.getColors()[index]);
                    int indexColor = (int)(Math.random()*7);
                    int textColor = Color.parseColor(tma.tanmuBean.getColors()[indexColor]);
                   // Log.v("makdebug", "tanmu--getMinTextSize--" + tma.tanmuBean.getMinTextSize());
                    //  Log.v("makdebug", "tanmu--content+textSize+textColor--" + content+"--"+textSize+"--"+textColor);

                    tma.showTanmu(content, textSize, textColor);
                }
            }
        }
    }

    //--------------------------分隔符--------------------------

    void showTanmu(String content, float textSize, int textColor) {
        final TextView textViewTanmu = new TextView(getActivity());
        textViewTanmu.setTextSize(textSize);
        textViewTanmu.setText(content);
        //  Log.v("makdebug", "tanmu--textViewTanmu.getParent--" + textViewTanmu.getParent());
        textViewTanmu.setTextColor(textColor);

        int leftMargin = containerRL.getRight() - containerRL.getLeft();
        int verticalMargin = getRandomTopMargin();
        textViewTanmu.setTag(verticalMargin);

        RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        pa.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        pa.topMargin = verticalMargin;
        textViewTanmu.setLayoutParams(pa);
        textViewTanmu.setGravity(Gravity.CENTER_HORIZONTAL);

        Animation anim = AnimationHelper.creatTranslateAnim(getActivity(), leftMargin, -ScreenUtils.getScreenW(getActivity()));
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                containerRL.removeView(textViewTanmu);
                int verticalMargin = (int) textViewTanmu.getTag();
                existMarginValues.remove(verticalMargin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textViewTanmu.startAnimation(anim);
        /**
         * 它竟然说我的【textViewTanmu】已经有【parent】了，我勒个去，赶快看看是哪里把【relative】变成它的parent了。
         * woca,把【textViewTanmu】放在【成员变量】那里就有parent，放在【showTanmu方法】里面就没parent了。
         */
//        View x = (View) textViewTanmu.getParent();
//        Log.v("makdebug", "textViewTanmu.getParent();--" + x);
        containerRL.addView(textViewTanmu);
        // Log.v("makdebug", "tanmu--containerRL.getChildCount--" + containerRL.getChildCount());
       // Log.v("makdebug", "tanmu--textViewTanmu.getParent--" + textViewTanmu.getParent());

    }

    //--------------------------分隔符--------------------------

    int getRandomTopMargin() {
        if (validHeightSpace == 0) {
            validHeightSpace = containerRL.getBottom() - containerRL.getTop();
        }
        if (linesCount == 0) {
            linesCount = validHeightSpace / ScreenUtils.dp2px(getActivity(),
                    tanmuBean.getMinTextSize() * (1 + tanmuBean.getRange()));
          //  Log.v("makdebug", "tanmu--linesCount--" + linesCount);
            if (linesCount == 0) {
                throw new RuntimeException("no place la");
            }
        }
        while (true) {
            int randomIndex = (int) (Math.random() * linesCount);
            int marginValue = randomIndex * (validHeightSpace / linesCount);
            if (!existMarginValues.contains(marginValue)) {
                existMarginValues.add(marginValue);
                //  Log.v("makdebug", "tanmu--marginValue--" + marginValue);
                return marginValue;
            }
        }
    }

    //--------------------------分隔符--------------------------

    class CreatTanmuThread implements Runnable {
        @Override
        public void run() {
            int N = tanmuBean.getItems().length;
            for (int i = 0; i < N; i++) {
                /**
                 * public final Message obtainMessage (int what, int arg1, int arg2)
                 * what	Value to assign to the returned Message.what field.
                   arg1	Value to assign to the returned Message.arg1 field.
                   arg2	Value to assign to the returned Message.arg2 field.
                 */
                handler.obtainMessage(1, i, 0).sendToTarget();
                SystemClock.sleep(500);
                //Log.v("makdebug", "tanmu---" + i);
            }
        }
    }
}
