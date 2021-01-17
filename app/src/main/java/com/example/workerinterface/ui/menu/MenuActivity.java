package com.example.workerinterface.ui.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.workerinterface.R;
import com.example.workerinterface.dto.ProductDTO;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuActivity extends AppCompatActivity {

    //Создаем список вьюх которые будут создаваться
    private List<View> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ViewFlipper vf = findViewById( R.id.viewFlipper );

        Button addButton = findViewById(R.id.test_product_button);
        //инициализировали наш массив с edittext.aьи
        allItems = new ArrayList<>();
        ArrayList<ProductDTO> productDTOS = new ArrayList<>();

        //находим наш linear который у нас под кнопкой add edittext в activity_main.xml
        final GridLayout table = findViewById(R.id.table);

        new Thread(() -> {
            try  {
                String IP = "192.168.1.130:8080";
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
                                    productDTOS.add(new ProductDTO(jsonarray.getJSONObject(i).getInt("id"),jsonarray.getJSONObject(i).get("name").toString(), (double) jsonarray.getJSONObject(i).get("price"),
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
            System.out.println(productDTOS.size());
            System.out.println(this);
            for (int i = 0; i < productDTOS.size(); i++) {
                System.out.println(i + " " + productDTOS.get(i).getName() + " " + productDTOS.get(i).getNameBuilding());

            //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
            @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_food_menu_item, null);

            //добавляем все что создаем в массив
            allItems.add(view);
            //добавляем елементы в linearlayout
            table.addView(view);

            TextView tv1 = findViewById(R.id.test_text);
            ImageView iv1 = findViewById(R.id.icon);

            byte[] imageBytes = productDTOS.get(i).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            iv1.setId(counter.get());
            iv1.setImageBitmap(bitmap);
            iv1.setMinimumWidth(bitmap.getWidth());
            iv1.setMinimumHeight(bitmap.getHeight());
            iv1.setMaxHeight(bitmap.getHeight());
            iv1.setMaxWidth(bitmap.getWidth());
            if (!productDTOS.get(i).getAvailable()) {
                tv1.setTextColor(Color.RED);
            }

            iv1.setOnClickListener(v1 -> {
                System.out.println("Listen" + iv1.getId());

                vf.showNext();

                RelativeLayout layoutDescription = findViewById(R.id.description);
                @SuppressLint("InflateParams") View descriptionView = getLayoutInflater().inflate(R.layout.custom_food_menu_descirption, null);

                layoutDescription.addView(descriptionView);

                TextView name = findViewById(R.id.text_description_nameValue);
                name.setText(productDTOS.get(iv1.getId()).getName());

                TextView description = findViewById(R.id.text_description_descValue);
                description.setText(productDTOS.get(iv1.getId()).getDescription());

                TextView available = findViewById(R.id.text_description_availableValue);
                available.setText(productDTOS.get(iv1.getId()).getAvailable().toString());

                available.setOnClickListener(v2 -> new Thread(() -> {

                    String USER_AGENT = "Mozilla/5.0";

                    HttpURLConnection con = null;
                    try {
                        Boolean status = productDTOS.get(iv1.getId()).getAvailable();
                        status = !status;

                        String urlParameters="available="+status.toString();
                        System.out.println(urlParameters);
                        int Id = productDTOS.get(iv1.getId()).getId();
                        String IP = "192.168.1.130:8080";
                        URL orderurl = new URL(new URL("http", IP, "/api/product/" + Id + "/available?" + urlParameters).toString().replace("[", "").replace("]", ""));
                        System.out.println(orderurl);
                        con = (HttpURLConnection) orderurl.openConnection();
                        con.setRequestMethod("PATCH");
                        con.setRequestProperty("User-Agent", USER_AGENT);

                        // For POST only - START
                        con.setDoOutput(true);
                        OutputStream os = con.getOutputStream();
                        os.write(urlParameters.getBytes());
                        System.out.println("---" + os.toString() + "---");
                        os.flush();
                        os.close();
                        // For POST only - END

                        int responseCode = con.getResponseCode();
                        System.out.println("PATCH Response Code :: " + responseCode);

                        if (responseCode == HttpURLConnection.HTTP_OK) { // success
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            if (!status) {
                                tv1.setTextColor(Color.RED);
                            } else {
                                tv1.setTextColor(Color.GRAY);
                            }
                            productDTOS.get(iv1.getId()).setAvailable(status);
                            available.setText(productDTOS.get(iv1.getId()).getAvailable().toString());

                            // print result
                            System.out.println(response.toString());
                            System.out.println(response);
                        } else {
                            System.out.println("PATCH request not worked");
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                        assert con != null;
                        con.disconnect();
                    }

                }).start());

                ImageView image = findViewById(R.id.description_image);
                image.setImageBitmap(bitmap);

                Button returnButton = findViewById(R.id.back_button);
                returnButton.setOnClickListener(v2 -> {
                    layoutDescription.removeAllViews();
                    vf.showPrevious();
                });

            });

            tv1.setId(counter.get());
            tv1.setText(productDTOS.get(counter.get()).getName());
            tv1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            counter.getAndIncrement();
            }
        });
    }
}