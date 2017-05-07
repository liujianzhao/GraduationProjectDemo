package com.example.graduationproject.entity;

/**
 * Created by liuji on 2017/5/2.
 */

public class Warning {

    private String closed;
    private String created;
    private int readout_id;
    private int warn_status;
    private int threshold_id;
    private int warn_type;

    public Warning() {
    }

    public Warning(String closed, String created, int readout_id, int warn_status, int threshold_id, int warn_type) {
        this.closed = closed;
        this.created = created;
        this.readout_id = readout_id;
        this.warn_status = warn_status;
        this.threshold_id = threshold_id;
        this.warn_type = warn_type;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getReadout_id() {
        return readout_id;
    }

    public void setReadout_id(int readout_id) {
        this.readout_id = readout_id;
    }

    public int getWarn_status() {
        return warn_status;
    }

    public void setWarn_status(int warn_status) {
        this.warn_status = warn_status;
    }

    public int getThreshold_id() {
        return threshold_id;
    }

    public void setThreshold_id(int threshold_id) {
        this.threshold_id = threshold_id;
    }

    public int getWarn_type() {
        return warn_type;
    }

    public void setWarn_type(int warn_type) {
        this.warn_type = warn_type;
    }
}
