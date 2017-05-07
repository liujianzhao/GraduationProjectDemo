package com.example.graduationproject.entity;

/**
 * Created by liuji on 2017/5/2.
 */

public class Nodeinfo {

    private int gateway_id;
    private int node_addr;
    private String node_name;
    private float X;
    private float Y;
    private String desc_string;
    private String pic;

    public Nodeinfo() {
    }

    public Nodeinfo(String pic, int gateway_id, int node_addr, String node_name, float x, float y, String desc_string) {
        this.pic = pic;
        this.gateway_id = gateway_id;
        this.node_addr = node_addr;
        this.node_name = node_name;
        X = x;
        Y = y;
        this.desc_string = desc_string;
    }

    public int getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(int gateway_id) {
        this.gateway_id = gateway_id;
    }

    public int getNode_addr() {
        return node_addr;
    }

    public void setNode_addr(int node_addr) {
        this.node_addr = node_addr;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
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
