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

import com.example.workerinterface.HTTPRequest;
import com.example.workerinterface.R;
import com.example.workerinterface.dto.ProductDTO;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ViewFlipper vf = findViewById( R.id.viewFlipper );

        ArrayList<ProductDTO> productDTOS = new ArrayList<>();

        HTTPRequest getRequest = new HTTPRequest();

        Thread getData = new Thread(() -> {
            int page = 0;
            try  {
                while (true) {
                    String receivedData = getRequest.getRequest("product", "", page);
                    JSONObject jsonObj = new JSONObject(receivedData);
                    JSONArray jsonarray = jsonObj.getJSONArray("content");
                    System.out.println(page + ";" + jsonarray.length());
                    if (jsonarray.length() == 0) break;
                        for (int i = 0; i < jsonarray.length(); i++) {
                            productDTOS.add(new ProductDTO(jsonarray.getJSONObject(i).getInt("id"),jsonarray.getJSONObject(i).get("name").toString(), (double) jsonarray.getJSONObject(i).get("price"),
                                    (Boolean) jsonarray.getJSONObject(i).get("available"), jsonarray.getJSONObject(i).get("description").toString(),
                                    jsonarray.getJSONObject(i).get("type").toString(), jsonarray.getJSONObject(i).get("image").toString(),
                                    jsonarray.getJSONObject(i).get("nameBuilding").toString()));
                            }
                    page++;
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        getData.start();
        try {
            getData.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Создаем список вьюх которые будут создаваться
        List<View> allItems = new ArrayList<>();

        //находим наш linear который у нас под кнопкой add edittext в activity_main.xml
        final GridLayout table = findViewById(R.id.table);

        int counter = 0;
        for (int i = 0; i < productDTOS.size(); i++) {
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
            iv1.setId(counter);
            iv1.setImageBitmap(bitmap);
            iv1.setMinimumWidth(bitmap.getWidth());
            iv1.setMinimumHeight(bitmap.getHeight());
            iv1.setMaxHeight(bitmap.getHeight());
            iv1.setMaxWidth(bitmap.getWidth());
            if (!productDTOS.get(i).getAvailable()) {
                tv1.setTextColor(Color.RED);
            }

            iv1.setOnClickListener(v1 -> {
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

                HTTPRequest httpRequest = new HTTPRequest();

                available.setOnClickListener(v2 -> {
                    Boolean status = productDTOS.get(iv1.getId()).getAvailable();
                    status = !status;

                    String urlParameters="available="+status.toString();

                    int id = productDTOS.get(iv1.getId()).getId();

                    new Thread(() -> httpRequest.patchRequest("product", urlParameters, id)).start();

                    if (!status) {
                        tv1.setTextColor(Color.RED);
                    } else {
                        tv1.setTextColor(Color.GRAY);
                    }
                    productDTOS.get(iv1.getId()).setAvailable(status);
                    available.setText(productDTOS.get(iv1.getId()).getAvailable().toString());
                });

                ImageView image = findViewById(R.id.description_image);
                image.setImageBitmap(bitmap);

                Button returnButton = findViewById(R.id.back_button);
                returnButton.setOnClickListener(v2 -> {
                    layoutDescription.removeAllViews();
                    vf.showPrevious();
                });

            });

            tv1.setId(counter);
            tv1.setText(productDTOS.get(counter).getName());
            tv1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            counter++;
        }
    }
}