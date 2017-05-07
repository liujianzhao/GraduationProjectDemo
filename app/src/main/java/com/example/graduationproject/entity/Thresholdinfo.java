package com.example.graduationproject.entity;

/**
 * Created by liuji on 2017/5/2.
 */

public class Thresholdinfo {

    private int id;
    private int channel;
    private int gateway_id;
    private int lower_limit;
    private int node_addr;
    private int upper_limit;


    public Thresholdinfo() {

    }

    public Thresholdinfo(int id, int channel, int gateway_id, int lower_limit, int node_addr, int upper_limit) {

        this.id = id;
        this.channel = channel;
        this.gateway_id = gateway_id;
        this.lower_limit = lower_limit;
        this.node_addr = node_addr;
        this.upper_limit = upper_limit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(int gateway_id) {
        this.gateway_id = gateway_id;
    }

    public int getLower_limit() {
        return lower_limit;
    }

    public void setLower_limit(int lower_limit) {
        this.lower_limit = lower_limit;
    }

    public int getNode_addr() {
        return node_addr;
    }

    public void setNode_addr(int node_addr) {
        this.node_addr = node_addr;
    }

    public int getUpper_limit() {
        return upper_limit;
    }

    public void setUpper_limit(int upper_limit) {
        this.upper_limit = upper_limit;
    }

}
