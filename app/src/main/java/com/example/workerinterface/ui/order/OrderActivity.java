package com.example.workerinterface.ui.order;

import android.annotation.SuppressLint;

import com.example.workerinterface.R;
import com.example.workerinterface.dto.OrderDTO;
import com.example.workerinterface.dto.ProductDTO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
import java.util.concurrent.atomic.AtomicReference;

public class OrderActivity extends AppCompatActivity {

    //Создаем список вьюх которые будут создаваться
    private List<View> allEds;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ViewFlipper vf = findViewById( R.id.order_viewFlipper );
        //инициализировали наш массив с edittext.aьи
        allEds = new ArrayList<>();
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();

        //находим наш linear который у нас под кнопкой add edittext в activity_main.xml
        final LinearLayout linear = findViewById(R.id.linear);

        Thread getData = new Thread(() -> {
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
        });
        getData.start();
        try {
            getData.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
        for (int i = 0; i < orderDTOS.size(); i++) {
            @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_order_info_gallery, null);
            TextView orderName = view.findViewById(R.id.orderName);
            Button doneButton = view.findViewById(R.id.order_button_done);
            Button cancelButton = view.findViewById(R.id.order_button_cancel);

            orderDTOS.get(i).getAll();

            doneButton.setId(i);
            orderName.setText(orderDTOS.get(i).getMonitorCode());
            //добавляем все что создаем в массив
            allEds.add(view);
            //добавляем елементы в linearlayout
            linear.addView(view);

            orderName.setOnClickListener(v1 -> {
                vf.showNext();

                GridLayout layoutDescription = findViewById(R.id.order_description);

                for (int is = 0; is < orderDTOS.get(doneButton.getId()).getProductIds().size(); is++) {

                    @SuppressLint("InflateParams") View descriptionView = getLayoutInflater().inflate(R.layout.custom_order_description, null);
                    int id = orderDTOS.get(doneButton.getId()).getProductIds().get(is);
                    layoutDescription.addView(descriptionView);

                    ProductDTO productDTO = null;
                    try {
                        productDTO = getRequest(id);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    assert productDTO != null;

                    TextView name = findViewById(R.id.text_description_order_nameValue);
                    name.setId(productDTO.getId());
                    name.setText(productDTO.getName());

                    ImageView image = findViewById(R.id.description_order_image);
                    byte[] imageBytes = productDTO.getImage();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    image.setId(productDTO.getId());
                    image.setImageBitmap(bitmap);
                    image.setMinimumWidth(bitmap.getWidth());
                    image.setMinimumHeight(bitmap.getHeight());
                    image.setMaxHeight(bitmap.getHeight());
                    image.setMaxWidth(bitmap.getWidth());

                    Button returnButton = findViewById(R.id.order_back_button);
                    returnButton.setId(is);
                    if (is != 0) {
                        ((ViewGroup) returnButton.getParent()).removeView(returnButton);
                    }

                    returnButton.setOnClickListener(v2 -> {
                        System.out.println(returnButton.getParent());
                        System.out.println(returnButton.getId());
                        layoutDescription.removeAllViews();
                        vf.showPrevious();
                    });


                }
            });

            doneButton.setOnClickListener(v1 -> {

                String urlParameters = "status=Ready";
                patchRequest(urlParameters);

                try {
                    ((LinearLayout) view.getParent()).removeView(view);
                    allEds.remove(view);
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            });

            cancelButton.setOnClickListener(v1 -> {

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

    private static ProductDTO getRequest(int id) throws InterruptedException {
        AtomicReference<ProductDTO> productDTO = new AtomicReference<>();
        Thread getData = new Thread(() -> {
            String USER_AGENT = "Mozilla/5.0";

            HttpURLConnection con = null;
            try {

                String IP = "192.168.1.130:8080";
                URL orderurl = new URL(new URL("http", IP, "/api/product?id=" + id).toString().replace("[", "").replace("]", ""));

                con = (HttpURLConnection) orderurl.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);

                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    try {
                        InputStream in = new BufferedInputStream(con.getInputStream());
                        BufferedReader r = new BufferedReader(new InputStreamReader(in));
                        String data = r.readLine();
                        JSONObject jsonObj = new JSONObject(data);

                        productDTO.set(new ProductDTO(jsonObj.getInt("id"), jsonObj.getString("name"),
                                jsonObj.getDouble("price"), jsonObj.getBoolean("available"),
                                jsonObj.getString("description"), jsonObj.getString("type"),
                                jsonObj.getString("image"), jsonObj.getString("nameBuilding")));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert con != null;
                con.disconnect();
            }
        });
        getData.start();
        getData.join();

        return productDTO.get();
    }
}