package com.atozmak.weatherlike;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class Introduce_container extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.main_orange);


        //TextView textView  = (TextView) findViewById(R.id.introduction_textView);

        ImageView imageView = (ImageView) findViewById(R.id.image_view_thank_you);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha0_1);
        imageView.startAnimation(animation);

        TextView textView = (TextView) findViewById(R.id.introduction_textView);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.introduction_scale);
        textView.startAnimation(animation1);

    }

}
