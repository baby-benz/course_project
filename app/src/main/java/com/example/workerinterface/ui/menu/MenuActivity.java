package com.example.workerinterface.ui.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.workerinterface.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    //Создаем список вьюх которые будут создаваться
    private List<View> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button addButton = (Button) findViewById(R.id.test_product_button);
        //инициализировали наш массив с edittext.aьи
        allItems = new ArrayList<>();

        //находим наш linear который у нас под кнопкой add edittext в activity_main.xml
        final GridLayout table = (GridLayout) findViewById(R.id.table);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    String IP = "192.168.1.130:8080";
                    URL url = null;
                    try {
                        url = new URL(new URL("http",IP, "/api/product/0").toString().replace("[","").replace("]",""));
                        System.out.println(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection)url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            try {
                                InputStream in = new BufferedInputStream(connection.getInputStream());
                                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                                StringBuilder total = new StringBuilder();
                                for (String line; (line = r.readLine()) != null; ) {
                                    System.out.println(line);
                                    total.append(line).append('\n');
                                }
                                JSONObject jsonObj = new JSONObject(total.toString());
                                System.out.println(jsonObj);
                                System.out.println(jsonObj.getJSONArray("content"));
                                JSONArray jsonarray = jsonObj.getJSONArray("content");
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    System.out.println(jsonarray.getJSONObject(i).get("id"));
                                    System.out.println(jsonarray.getJSONObject(i).get("name"));
                                    System.out.println(jsonarray.getJSONObject(i).get("price"));
                                    System.out.println(jsonarray.getJSONObject(i).get("available"));
                                    System.out.println(jsonarray.getJSONObject(i).get("description"));
                                }
                            } finally {
                                connection.disconnect();
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("ERROR");
                        e.printStackTrace();
                    } finally {
                        try {
                            System.out.println(connection.getResponseCode());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        addButton.setOnClickListener(v -> {
            //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
            @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_food_menu_item, null);

            //добавляем все что создаем в массив
            allItems.add(view);
            //добавляем елементы в linearlayout
            table.addView(view);
        });
    }


}