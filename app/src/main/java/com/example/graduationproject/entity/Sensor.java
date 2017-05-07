package com.example.graduationproject.entity;

/**
 * Created by liuji on 2017/5/2.
 */

public class Sensor {

    private int channel;
    private int data_type;
    private int sensor_type;
    private int value;
    private int id;
    private String created;
    private int gateway_id;
    private int node_addr;

    public Sensor() {
    }

    public Sensor(int channel, int data_type, int sensor_type, int value, int id, String created, int gateway_id, int node_addr) {

        this.channel = channel;
        this.data_type = data_type;
        this.sensor_type = sensor_type;
        this.value = value;
        this.id = id;
        this.created = created;
        this.gateway_id = gateway_id;
        this.node_addr = node_addr;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }

    public int getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(int sensor_type) {
        this.sensor_type = sensor_type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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
}
