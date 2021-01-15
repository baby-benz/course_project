package com.example.course_project.ui.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.course_project.R;
import com.example.course_project.data.model.Menu;
import com.example.course_project.dto.ProductDto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menu, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<ProductDto> productDtos = new ArrayList<>();
        new Thread(() -> {
            try  {
                String IP = "192.168.0.5:8080";
                int page = 0;
                URL producturl;
                while (true) {
                    producturl = new URL(new URL("http", IP, "/api/product/" + page).toString().replace("[", "").replace("]", ""));
                    System.out.println(producturl);
                    HttpURLConnection connection;
                    try {
                        connection = (HttpURLConnection) producturl.openConnection();
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            try {
                                InputStream in = new BufferedInputStream(connection.getInputStream());
                                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                                StringBuilder total = new StringBuilder();
                                for (String line; (line = r.readLine()) != null; ) {
                                    total.append(line).append('\n');
                                }
                                JSONObject jsonObj = new JSONObject(total.toString());

                                JSONArray jsonarray = jsonObj.getJSONArray("content");
                                System.out.println(jsonarray.length());
                                if (jsonarray.length() == 0) break;
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    productDtos.add(new ProductDto(jsonarray.getJSONObject(i).get("name").toString(), (double) jsonarray.getJSONObject(i).get("price"),
                                            (Boolean) jsonarray.getJSONObject(i).get("available"), jsonarray.getJSONObject(i).get("description").toString(),
                                            jsonarray.getJSONObject(i).get("type").toString(), jsonarray.getJSONObject(i).get("image").toString(),
                                            jsonarray.getJSONObject(i).get("nameBuilding").toString()));
                                }
                            } finally {
                                connection.disconnect();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    page++;
                }
                System.out.println("finished downloading data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println(productDtos.get(0).getName());
        for (ProductDto productDto : productDtos) {
            System.out.println(productDto.getName());
        }
        List<Menu.MenuItem> menuItems = Menu.ITEMS;

        RecyclerView rvNovelties = view.findViewById(R.id.rvNovelties);
        RecyclerView rvPopular = view.findViewById(R.id.rvPopular);
        RecyclerView rvBreakfasts = view.findViewById(R.id.rvBreakfasts);
        RecyclerView rvStarters = view.findViewById(R.id.rvStarters);
        RecyclerView rvSeconds = view.findViewById(R.id.rvSeconds);

        Context context = getContext();

        rvNovelties.setAdapter(new MenuAdapter(context, menuItems));
        rvPopular.setAdapter(new MenuAdapter(context, menuItems));
        rvBreakfasts.setAdapter(new MenuAdapter(context, menuItems));
        rvStarters.setAdapter(new MenuAdapter(context, menuItems));
        rvSeconds.setAdapter(new MenuAdapter(context, menuItems));

        HorizontalScrollView horizontalScrollView = view.findViewById(R.id.horizontal_menu);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);

        final Button noveltiesButton = view.findViewById(R.id.novelties);
        final Button popularButton = view.findViewById(R.id.popular);
        final Button breakfastButton = view.findViewById(R.id.breakfast);
        final Button starterButton = view.findViewById(R.id.starter);
        final Button secondButton = view.findViewById(R.id.second);

        noveltiesButton.setOnClickListener(v -> {
            int titleTop = getRelativeTop(view.findViewById(R.id.novelties_title));
            view.findViewById(R.id.fragment_menu).scrollTo(0, titleTop);
        });

        popularButton.setOnClickListener(v -> {
            int titleTop = getRelativeTop(view.findViewById(R.id.popular_title));
            view.findViewById(R.id.fragment_menu).scrollTo(0, titleTop);
        });

        breakfastButton.setOnClickListener(v -> {
            int titleTop = getRelativeTop(view.findViewById(R.id.breakfasts_title));
            view.findViewById(R.id.fragment_menu).scrollTo(0, titleTop);
        });

        starterButton.setOnClickListener(v -> {
            int titleTop = getRelativeTop(view.findViewById(R.id.starters_title));
            view.findViewById(R.id.fragment_menu).scrollTo(0, titleTop);
        });

        secondButton.setOnClickListener(v -> {
            int titleTop = getRelativeTop(view.findViewById(R.id.seconds_title));
            view.findViewById(R.id.fragment_menu).scrollTo(0, titleTop);
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