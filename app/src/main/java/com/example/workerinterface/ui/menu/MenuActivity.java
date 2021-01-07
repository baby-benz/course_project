package com.example.workerinterface.ui.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.workerinterface.R;

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