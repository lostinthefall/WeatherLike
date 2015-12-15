package com.atozmak.weatherlike;

import android.graphics.Color;

/**
 * Created by Mak on 2015/12/4.
 */
public class TanmuBean {
    private String[] items;
    private int color;
    private int minTextSize;
    private float range;
    private  String[] colors;

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public TanmuBean() {
        color = Color.parseColor("#444444");
        minTextSize = 25;
        range = 0.5f;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getMinTextSize() {
        return minTextSize;
    }

    public void setMinTextSize(int minTextSize) {
        this.minTextSize = minTextSize;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
}
