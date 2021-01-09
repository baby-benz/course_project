package com.example.course_project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.example.course_project.ui.login.LoginFragment;
import com.example.course_project.ui.menu.MenuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.main_container_view, new MenuFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.menu:
                            fragmentManager.beginTransaction().replace(R.id.main_container_view, new MenuFragment()).commit();
                            break;
                        case R.id.profile:
                            fragmentManager.beginTransaction().replace(R.id.main_container_view, new LoginFragment()).commit();
                            break;
                        case R.id.more:
                            break;
                        default:
                    }
                    return false;
                }
        );
    }
}