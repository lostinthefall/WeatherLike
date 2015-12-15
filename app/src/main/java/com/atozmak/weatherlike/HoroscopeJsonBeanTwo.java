package com.atozmak.weatherlike;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mak on 2015/11/28.
 */
public class HoroscopeJsonBeanTwo {


    private String  total;
    private String job;
    private String love;
    private String money;
    private String sign;

    public String getTotal() {
        return total;
    }

    public String getJob() {
        return job;
    }

    public String getLove() {
        return love;
    }

    public String getMoney() {
        return money;
    }

    public String getSign() {
        return sign;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
