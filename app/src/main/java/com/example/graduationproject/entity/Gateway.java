package com.example.graduationproject.entity;

/**
 * Created by liuji on 2017/5/2.
 */

public class Gateway {

    private String name;
    private String ip;
    private int port;
    private int max_nodes;
    private int max_channels;
    private int poll_interval;
    private float X;
    private float Y;
    private String desc_string;
    private String pic;


    public Gateway() {
    }

    public Gateway(String name, String pic, String ip, int port, int max_nodes, int max_channels, int poll_interval, float x, float y, String desc_string) {
        this.name = name;
        this.pic = pic;
        this.ip = ip;
        this.port = port;
        this.max_nodes = max_nodes;
        this.max_channels = max_channels;
        this.poll_interval = poll_interval;
        X = x;
        Y = y;
        this.desc_string = desc_string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMax_nodes() {
        return max_nodes;
    }

    public void setMax_nodes(int max_nodes) {
        this.max_nodes = max_nodes;
    }

    public int getMax_channels() {
        return max_channels;
    }

    public void setMax_channels(int max_channels) {
        this.max_channels = max_channels;
    }

    public int getPoll_interval() {
        return poll_interval;
    }

    public void setPoll_interval(int poll_interval) {
        this.poll_interval = poll_interval;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public String getDesc_string() {
        return desc_string;
    }

    public void setDesc_string(String desc_string) {
        this.desc_string = desc_string;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
