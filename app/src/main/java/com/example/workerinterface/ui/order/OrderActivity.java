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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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
                    HttpURLConnection connection = null;
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

                            for (int i = 0; i < jsonarray.length(); i++) {
                                orderDTOS.add(new OrderDTO(jsonarray.getJSONObject(i).get("orderedOn").toString(),
                                        jsonarray.getJSONObject(i).get("monitorCode").toString(),
                                        jsonarray.getJSONObject(i).getString("status"), jsonarray.getJSONObject(i).getJSONArray("productIds"),
                                        jsonarray.getJSONObject(i).getString("buildingName"), jsonarray.getJSONObject(i).getInt("userId")));
                            }

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    } finally {
                        connection.disconnect();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                page++;
            }
            for (int i = 0; i < orderDTOS.size(); i++) {
                System.out.println(orderDTOS.get(i).getOrderedOn() + " " + orderDTOS.get(i).getMonitorCode() + " " + orderDTOS.get(i).getProductIds());
            }
        }).start();



        addButton.setOnClickListener(v -> {
            counter++;

            //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
            @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_order_info_gallery, null);
            Button deleteField = (Button) view.findViewById(R.id.order_button_done);
            deleteField.setOnClickListener(v1 -> {

                new Thread(() -> {

                    String USER_AGENT = "Mozilla/5.0";

                    String urlParameters = "status=Created";
                    HttpURLConnection con = null;
                    try {

                        String IP = "192.168.1.130:8080";
                        URL orderurl = new URL(new URL("http", IP, "/api/order/1/status?" + urlParameters).toString().replace("[", "").replace("]", ""));
                        con = (HttpURLConnection) orderurl.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("User-Agent", USER_AGENT);

                        // For POST only - START
                        con.setDoOutput(true);
                        OutputStream os = con.getOutputStream();
                        os.write(urlParameters.getBytes());
                        os.flush();
                        os.close();
                        // For POST only - END

                        int responseCode = con.getResponseCode();
                        System.out.println("POST Response Code :: " + responseCode);

                        if (responseCode == HttpURLConnection.HTTP_OK) { // success
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            } in.close();

                            // print result
                            System.out.println(response.toString());
                        } else {
                            System.out.println("POST request not worked");
                        }

//                        String IP = "192.168.1.130:8080";
//                        URL orderurl = new URL(new URL("http", IP, "/api/order/1/status?" + urlParameters).toString().replace("[", "").replace("]", ""));
//
//                        con = (HttpURLConnection) orderurl.openConnection();
//                        con.setRequestMethod("POST"); // PUT is another valid option
//                        con.setDoOutput(true);


                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                        con.disconnect();
                    }

                }).start();

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