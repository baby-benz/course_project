package com.example.course_project;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import carbon.view.View;
import com.example.course_project.ui.cart.CartFragment;
import com.example.course_project.ui.login.LoginFragment;
import com.example.course_project.ui.menu.MenuFragment;
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
                }).check();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.main_container_view, new MenuFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.menu:
                            fragmentManager.beginTransaction().replace(R.id.main_container_view, new MenuFragment()).commit();
                            findViewById(R.id.main_app_bar).setVisibility(View.VISIBLE);
                            break;
                        case R.id.profile:
                            fragmentManager.beginTransaction().replace(R.id.main_container_view, new LoginFragment()).commit();
                            findViewById(R.id.main_app_bar).setVisibility(View.VISIBLE);
                            break;
                        case R.id.cart:
                            fragmentManager.beginTransaction().replace(R.id.main_container_view, new CartFragment()).commit();
                            findViewById(R.id.main_app_bar).setVisibility(View.GONE);
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