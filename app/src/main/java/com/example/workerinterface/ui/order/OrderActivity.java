package com.example.workerinterface.ui.order;

import android.annotation.SuppressLint;
import android.widget.EditText;

import com.example.workerinterface.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

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

        //находим наш linear который у нас под кнопкой add edittext в activity_main.xml
        final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
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