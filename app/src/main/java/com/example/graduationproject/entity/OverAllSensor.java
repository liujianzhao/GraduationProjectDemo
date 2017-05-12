package com.example.graduationproject.entity;

/**
 * Created by liuji on 2017/5/12.
 */

public class OverAllSensor {

    private int normalData;
    private int highData;
    private int errorData;

    public OverAllSensor() {
    }

    public OverAllSensor(int normalData, int errorData, int highData) {
        this.normalData = normalData;
        this.errorData = errorData;
        this.highData = highData;
    }

    public int getNormalData() {
        return normalData;
    }

    public void setNormalData(int normalData) {
        this.normalData = normalData;
    }

    public int getHighData() {
        return highData;
    }

    public void setHighData(int highData) {
        this.highData = highData;
    }

    public int getErrorData() {
        return errorData;
    }

    public void setErrorData(int errorData) {
        this.errorData = errorData;
    }
}
