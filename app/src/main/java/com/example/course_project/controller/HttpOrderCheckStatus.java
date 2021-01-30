package com.example.course_project.controller;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.course_project.event.notification.NotificationBox;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;

import java.util.concurrent.Callable;

public class HttpOrderCheckStatus implements Callable {
    public void getStatusOrder(Long orderId) {
        AndroidNetworking.get("http://192.168.0.5:8080/api/order?orderId=" + orderId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("status").equals("готов") || response.get("status").equals("отменен"))
                                NotificationBox.showNotification("Ваш заказ", response.getString("status") + " monitor code:" + response.getString("monitorCode"));
                            else {final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("order with id: "+ orderId + "not prepared yet");
                                        getStatusOrder(orderId);
                                    }
                                }, 8000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    @Override
    public HttpOrderCheckStatus call() throws Exception {
        return this;
    }
}
