package com.example.workerinterface.dto;

import java.util.ArrayList;

public class OrderDTO {
    public OrderDTO(String orderedOn, String monitorCode, String status, ArrayList<Integer> productIds,
                    String nameBuilding, Integer userId) {
        this.orderedOn = orderedOn;
        this.monitorCode = monitorCode;
        this.status = status;
        this.productIds = productIds;
        this.nameBuilding = nameBuilding;
        this.userId = userId;
    }

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
}
