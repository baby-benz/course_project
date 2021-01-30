package com.example.course_project;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import com.androidnetworking.AndroidNetworking;
import com.example.course_project.data.db.cart.CartDataSource;
import com.example.course_project.data.db.cart.CartDatabase;
import com.example.course_project.data.db.cart.LocalCartDataSource;
import com.example.course_project.data.model.Common;
import com.example.course_project.event.SuccessfulLogin;
import com.example.course_project.event.notification.NotificationBox;
import com.example.course_project.ui.cart.CartFragment;
import com.example.course_project.ui.login.LoginFragment;
import com.example.course_project.ui.menu.MenuFragment;
import com.example.course_project.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();

        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Невозможно определить местоположение в автоматическом режиме", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.main_container_view, new MenuFragment()).commit();

        MenuFragment menuFragment = new MenuFragment();
        LoginFragment loginFragment = new LoginFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        CartFragment cartFragment = new CartFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    if (item.isChecked()) {
                        if (item.getItemId() == R.id.menu) {
                            NestedScrollView scrollView = findViewById(R.id.svMenu);
                            if (scrollView != null) {
                                scrollView.smoothScrollTo(0, 0);
                            }
                        }
                        return false;
                    }
                    item.setChecked(true);
                    switch (item.getItemId()) {
                        case R.id.menu:
                            fragmentManager.beginTransaction().replace(R.id.main_container_view, menuFragment).commit();
                            findViewById(R.id.main_app_bar).setVisibility(View.VISIBLE);
                            break;
                        case R.id.profile:
                            if (Common.LOGGED_IN_USER == null) {
                                fragmentManager.beginTransaction().replace(R.id.main_container_view, loginFragment).commit();
                            } else {
                                fragmentManager.beginTransaction().replace(R.id.main_container_view, profileFragment).commit();
                            }
                            findViewById(R.id.main_app_bar).setVisibility(View.VISIBLE);
                            break;
                        case R.id.cart:
                            fragmentManager.beginTransaction().replace(R.id.main_container_view, cartFragment).commit();
                            findViewById(R.id.main_app_bar).setVisibility(View.GONE);
                            break;
                        /*case R.id.more:
                            break;*/
                        default:
                    }
                    return false;
                }
        );
    }


}