package com.example.workerinterface.ui.order;

import android.annotation.SuppressLint;

import com.example.workerinterface.R;
import com.example.workerinterface.dto.OrderDTO;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    //Создаем список вьюх которые будут создаваться
    private List<View> allEds;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Button addButton = findViewById(R.id.order_button);
        //инициализировали наш массив с edittext.aьи
        allEds = new ArrayList<>();
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();

        //находим наш linear который у нас под кнопкой add edittext в activity_main.xml
        final LinearLayout linear = findViewById(R.id.linear);

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
                        assert connection != null;
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

            //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
            for (int i = 0; i < orderDTOS.size(); i++) {
                @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_order_info_gallery, null);
                Button doneField = view.findViewById(R.id.order_button_done);
                Button cancelledField = view.findViewById(R.id.order_button_cancel);

                orderDTOS.get(i).getAll();

                doneField.setId(orderDTOS.get(i).getUserId());
                TextView text = view.findViewById(R.id.editText);
                text.setText(orderDTOS.get(i).getMonitorCode());
                //добавляем все что создаем в массив
                allEds.add(view);
                //добавляем елементы в linearlayout
                linear.addView(view);

                doneField.setOnClickListener(v1 -> {

                    String urlParameters = "status=Ready";
                    patchRequest(urlParameters);

                    try {
                        ((LinearLayout) view.getParent()).removeView(view);
                        allEds.remove(view);
                    } catch (IndexOutOfBoundsException ex) {
                        ex.printStackTrace();
                    }
                });

                cancelledField.setOnClickListener(v1 -> {

                    String urlParameters = "status=Cancelled";
                    patchRequest(urlParameters);

                    try {
                        ((LinearLayout) view.getParent()).removeView(view);
                        allEds.remove(view);
                    } catch (IndexOutOfBoundsException ex) {
                        ex.printStackTrace();
                    }

                });

            }

        });
    }

    private static void patchRequest(String urlParameters) {
        new Thread(() -> {

            String USER_AGENT = "Mozilla/5.0";

            HttpURLConnection con = null;
            try {

                String IP = "192.168.1.130:8080";
                URL orderurl = new URL(new URL("http", IP, "/api/order/1/status?" + urlParameters).toString().replace("[", "").replace("]", ""));
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

        }).start();
    }
}