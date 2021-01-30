package com.example.workerinterface.dto;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class OrderDTO {
    public OrderDTO(int id, String orderedOn, String monitorCode, String status, JSONArray productIds,
                    String nameBuilding, Integer userId) throws JSONException {
        this.id = id;
        this.orderedOn = orderedOn;
        this.monitorCode = monitorCode;
        this.status = status;
        this.productIds = new ArrayList<>();
        for (int i = 0; i < productIds.length(); i++) {
            this.productIds.add((Integer)productIds.get(i));
        }
        this.nameBuilding = nameBuilding;
        this.userId = userId;
    }

    private int id;
    private String orderedOn;
    private String monitorCode;
    private String status;
    private ArrayList<Integer> productIds;
    private String nameBuilding;
    private Integer userId;

    public String getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(String orderedOn) {
        this.orderedOn = orderedOn;
    }

    public String getMonitorCode() {
        return monitorCode;
    }

    public void setMonitorCode(String monitorCode) {
        this.monitorCode = monitorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(ArrayList<Integer> productIds) {
        this.productIds = productIds;
    }

    public String getNameBuilding() {
        return nameBuilding;
    }

    public void setNameBuilding(String nameBuilding) {
        this.nameBuilding = nameBuilding;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
