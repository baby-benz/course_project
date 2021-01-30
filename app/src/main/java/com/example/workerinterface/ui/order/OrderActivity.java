package com.example.workerinterface.ui.order;

import android.annotation.SuppressLint;

import com.example.workerinterface.HTTPRequest;
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
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
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

        HTTPRequest getRequest = new HTTPRequest();

        Thread getData = new Thread(() -> {
            int page = 0;

            while (true) {
                String receivedData = getRequest.getRequest("order", "", page);
                try {
                    JSONObject jsonObj = new JSONObject(receivedData);
                    JSONArray jsonArray = jsonObj.getJSONArray("content");
                    if (jsonArray.length() == 0) break;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        orderDTOS.add(new OrderDTO(jsonArray.getJSONObject(i).getInt("id"),jsonArray.getJSONObject(i).getString("orderedOn"),
                                jsonArray.getJSONObject(i).getString("monitorCode"),
                                jsonArray.getJSONObject(i).getString("status"), jsonArray.getJSONObject(i).getJSONArray("productIds"),
                                jsonArray.getJSONObject(i).getString("buildingName"), jsonArray.getJSONObject(i).getInt("userId")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                page++;
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
            orderName.setText(orderDTOS.get(i).getMonitorCode());
            Button doneButton = view.findViewById(R.id.order_button_done);
            doneButton.setId(orderDTOS.get(i).getId());
            Button cancelButton = view.findViewById(R.id.order_button_cancel);
            cancelButton.setId(orderDTOS.get(i).getId());

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

                    ProductDTO productDTO = getRequest(id);

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
                        layoutDescription.removeAllViews();
                        vf.showPrevious();
                    });

                }
            });

            doneButton.setOnClickListener(v1 -> {

                String urlParameters = "status=Ready";
                System.out.println("ID " + doneButton.getId());
                HTTPRequest httpRequest = new HTTPRequest();
                new Thread(() -> httpRequest.patchRequest("order", urlParameters, doneButton.getId())).start();

                try {
                    ((LinearLayout) view.getParent()).removeView(view);
                    allEds.remove(view);
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            });

            cancelButton.setOnClickListener(v1 -> {

                String urlParameters = "status=Cancelled";
                System.out.println("ID: " + doneButton.getId());
                HTTPRequest httpRequest = new HTTPRequest();
                new Thread(() -> httpRequest.patchRequest("order", urlParameters, doneButton.getId())).start();

                try {
                    ((LinearLayout) view.getParent()).removeView(view);
                    allEds.remove(view);
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }

            });

        }
    }

    @NotNull
    private static ProductDTO getRequest(int id) {
        ProductDTO productDTO = new ProductDTO();

        HTTPRequest getRequest = new HTTPRequest();
        Thread getData = new Thread(() -> {

            String urlParameters = "?id=";
            String receivedData = getRequest.getRequest("product", urlParameters, id);

            try {
                JSONObject jsonObj = new JSONObject(receivedData);
                productDTO.setId(jsonObj.getInt("id"));
                productDTO.setName(jsonObj.getString("name"));
                productDTO.setAvailable(jsonObj.getBoolean("available"));
                productDTO.setDescription(jsonObj.getString("description"));
                productDTO.setType(jsonObj.getString("type"));
                productDTO.setImage(jsonObj.getString("image"));
                productDTO.setNameBuilding(jsonObj.getString("nameBuilding"));
                productDTO.setPrice((float)jsonObj.getDouble("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        getData.start();
        try {
            getData.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return productDTO;
    }
}