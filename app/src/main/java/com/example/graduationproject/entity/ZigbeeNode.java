package com.example.graduationproject.entity;

/**
 * Created by liuji on 2017/5/2.
 */

public class ZigbeeNode {
    private String created;
    private int node_addr;
    private String node_name;
    private int node_online;
    private int gateway_id;

    public ZigbeeNode() {
    }

    public ZigbeeNode(String created, int node_addr, String node_name, int node_online, int gateway_id) {
        this.created = created;
        this.node_addr = node_addr;
        this.node_name = node_name;
        this.node_online = node_online;
        this.gateway_id = gateway_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    public int getNode_online() {
        return node_online;
    }

    public void setNode_online(int node_online) {
        this.node_online = node_online;
    }

    public int getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(int gateway_id) {
        this.gateway_id = gateway_id;
    }
}
