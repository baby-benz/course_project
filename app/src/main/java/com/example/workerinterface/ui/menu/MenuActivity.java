package com.example.workerinterface.ui.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workerinterface.R;
import com.example.workerinterface.dto.ProductDTO;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        ArrayList<ProductDTO> productDTOS = new ArrayList<>();

        //находим наш linear который у нас под кнопкой add edittext в activity_main.xml
        final GridLayout table = (GridLayout) findViewById(R.id.table);

        new Thread(() -> {
            try  {
                String IP = "192.168.0.5:8080";
                int page = 0;
                URL producturl;
                while (true) {
                    producturl = new URL(new URL("http", IP, "/api/product/" + page).toString().replace("[", "").replace("]", ""));
                    System.out.println(producturl);
                    HttpURLConnection connection;
                    try {
                        connection = (HttpURLConnection) producturl.openConnection();
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            try {
                                InputStream in = new BufferedInputStream(connection.getInputStream());
                                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                                StringBuilder total = new StringBuilder();
                                for (String line; (line = r.readLine()) != null; ) {
                                    total.append(line).append('\n');
                                }
                                JSONObject jsonObj = new JSONObject(total.toString());

                                JSONArray jsonarray = jsonObj.getJSONArray("content");
                                System.out.println(jsonarray.length());
                                if (jsonarray.length() == 0) break;
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    productDTOS.add(new ProductDTO(jsonarray.getJSONObject(i).get("name").toString(), (double) jsonarray.getJSONObject(i).get("price"),
                                            (Boolean) jsonarray.getJSONObject(i).get("available"), jsonarray.getJSONObject(i).get("description").toString(),
                                            jsonarray.getJSONObject(i).get("type").toString(), jsonarray.getJSONObject(i).get("image").toString(),
                                            jsonarray.getJSONObject(i).get("nameBuilding").toString()));
                                }
                            } finally {
                                connection.disconnect();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                page++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        AtomicInteger counter = new AtomicInteger();
        addButton.setOnClickListener(v -> {
            for (int i = 0; i < productDTOS.size(); i++) {
                System.out.println(i + " " + productDTOS.get(i).getName() + " " + productDTOS.get(i).getNameBuilding());

            //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
            @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_food_menu_item, null);

            //добавляем все что создаем в массив
            allItems.add(view);
            //добавляем елементы в linearlayout
            table.addView(view);

            TextView tv1 = (TextView) findViewById(R.id.test_text);
            ImageView iv1 = (ImageView) findViewById(R.id.icon);

            byte[] imageBytes = productDTOS.get(i).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            iv1.setId(counter.get());
            iv1.setImageBitmap(bitmap);

            tv1.setId(counter.get());
            tv1.setText(productDTOS.get(counter.get()).getName());
            tv1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            counter.getAndIncrement();
            }
        });
    }
}