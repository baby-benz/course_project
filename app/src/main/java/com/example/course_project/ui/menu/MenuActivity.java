package com.example.course_project.ui.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.course_project.MenuListFragment;
import com.example.course_project.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontal_menu);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.novelties_list_container_view, new MenuListFragment())
                .add(R.id.popular_list_container_view, new MenuListFragment())
                .add(R.id.breakfast_list_container_view, new MenuListFragment())
                .add(R.id.starter_list_container_view, new MenuListFragment())
                .add(R.id.second_list_container_view, new MenuListFragment())
                .commit();

        final Button noveltiesButton = findViewById(R.id.novelties);
        final Button popularButton = findViewById(R.id.popular);
        final Button breakfastButton = findViewById(R.id.breakfast);
        final Button starterButton = findViewById(R.id.starter);
        final Button secondButton = findViewById(R.id.second);

        noveltiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int titleTop = getRelativeTop(findViewById(R.id.novelties_title));
                findViewById(R.id.menu_scroll_view).scrollTo(0, titleTop);
            }
        });

        popularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int titleTop = getRelativeTop(findViewById(R.id.popular_title));
                findViewById(R.id.menu_scroll_view).scrollTo(0, titleTop);
            }
        });

        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int titleTop = getRelativeTop(findViewById(R.id.breakfast_title));
                findViewById(R.id.menu_scroll_view).scrollTo(0, titleTop);
            }
        });

        starterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int titleTop = getRelativeTop(findViewById(R.id.starter_title));
                findViewById(R.id.menu_scroll_view).scrollTo(0, titleTop);
            }
        });

        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int titleTop = getRelativeTop(findViewById(R.id.second_title));
                findViewById(R.id.menu_scroll_view).scrollTo(0, titleTop);
            }
        });
    }

    private int getRelativeTop(View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getTop();
        } else {
            return view.getTop() + getRelativeTop((View) view.getParent());
        }
    }
}