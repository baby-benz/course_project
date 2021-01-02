package com.example.course_project.ui.menu;

import android.os.Bundle;
import android.widget.HorizontalScrollView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.course_project.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontal_menu);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
    }
}