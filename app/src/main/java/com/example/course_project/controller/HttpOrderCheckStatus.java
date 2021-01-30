package com.example.course_project.controller;

import android.os.AsyncTask;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.course_project.event.notification.NotificationBox;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpOrderCheckStatus {
    public void getStatusOrder(Long orderId) {
        AndroidNetworking.get("http://192.168.0.5:8080/api/order?orderId=" + orderId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("status").equals("готов") || response.get("status").equals("отменен"))
                                NotificationBox.showNotification("Ваш заказ", response.getString("status") + " monitor code:" + response.getString("monitorCode"));
                            else {
                                Thread.sleep(10000);
                                System.out.println("order with id: "+ orderId + "not prepared yet");
                                getStatusOrder(orderId);
                            }
                        } catch (JSONException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}
