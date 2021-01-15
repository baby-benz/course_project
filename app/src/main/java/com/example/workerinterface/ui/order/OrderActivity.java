package com.example.workerinterface.ui.order;

import android.annotation.SuppressLint;
import android.widget.EditText;

import com.example.workerinterface.R;
import com.example.workerinterface.dto.OrderDTO;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    //Создаем список вьюх которые будут создаваться
    private List<View> allEds;
    //счетчик чисто декоративный для визуального отображения edittext'ov
    private int counter = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Button addButton = (Button) findViewById(R.id.order_button);
        //инициализировали наш массив с edittext.aьи
        allEds = new ArrayList<>();
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();

        //находим наш linear который у нас под кнопкой add edittext в activity_main.xml
        final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);

        new Thread(() -> {
            URL orderurl;
            int page = 0;
            String IP = "192.168.1.130:8080";
            while (true) {
                try {
                    orderurl = new URL(new URL("http", IP, "/api/order/" + page).toString().replace("[", "").replace("]", ""));
                    System.out.println(orderurl);
                    HttpURLConnection connection;
                    try {
                        connection = (HttpURLConnection) orderurl.openConnection();
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream in = new BufferedInputStream(connection.getInputStream());
                            BufferedReader r = new BufferedReader(new InputStreamReader(in));
                            StringBuilder total = new StringBuilder();
                            for (String line; (line = r.readLine()) != null; ) {
                                total.append(line).append('\n');
                            }
                            JSONObject jsonObj = new JSONObject(total.toString());

                            JSONArray jsonarray = jsonObj.getJSONArray("content");
                            if (jsonarray.length() == 0) break;
                            System.out.println(jsonarray.length());
                            for (int i = 0; i < jsonarray.length(); i++) {
//                                orderDTOS.add(new OrderDTO(jsonarray.getJSONObject(i).get("orderedOn").toString(),
//                                        jsonarray.getJSONObject(i).get("monitorCode").toString(),
//                                        jsonarray.getJSONObject(i).getString("status")))
                                System.out.println(jsonarray.getJSONObject(i).get("productIds").toString());
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                page++;
            }
        }).start();


        addButton.setOnClickListener((View.OnClickListener) v -> {
            counter++;

            //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
            @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_order_info_gallery, null);
            Button deleteField = (Button) view.findViewById(R.id.order_button_done);
            deleteField.setOnClickListener(v1 -> {
                try {
                    ((LinearLayout) view.getParent()).removeView(view);
                    allEds.remove(view);
                } catch(IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            });
            EditText text = (EditText) view.findViewById(R.id.editText);
            text.setText("Some order " + counter);
            //добавляем все что создаем в массив
            allEds.add(view);
            //добавляем елементы в linearlayout
            linear.addView(view);
        });
    }
}