package com.example.course_project.ui.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.course_project.R;
import com.example.course_project.data.model.Menu;
import com.example.course_project.dto.ProductDto;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuFragment extends Fragment {

    volatile ArrayList<ProductDto> productDtos;
    volatile ArrayList<ProductDto> breakfastDtos;
    volatile ArrayList<ProductDto> starterDtos;
    volatile ArrayList<ProductDto> secondDtos;
    volatile ArrayList<ProductDto> drinkingDtos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menu, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productDtos = new ArrayList<>();
        breakfastDtos = new ArrayList<>();
        starterDtos = new ArrayList<>();
        secondDtos = new ArrayList<>();
        drinkingDtos = new ArrayList<>();

        new Thread(() -> {
            try  {
                productDtos = getProductFromServer("/api/product/");
                } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try  {
                breakfastDtos = getProductFromServer("/api/product/type/breakfast/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try  {
                starterDtos = getProductFromServer("/api/product/type/starter/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try  {
                secondDtos = getProductFromServer("/api/product/type/second/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try  {
                drinkingDtos = getProductFromServer("/api/product/type/drinking/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        while (productDtos.size() == 0
                || breakfastDtos.size() == 0
                || starterDtos.size() == 0
                || secondDtos.size() == 0
                || drinkingDtos.size() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        List<Menu.MenuItem> menuItems = Menu.ITEMS;
        List<Menu.MenuItem> menuItems = new ArrayList<>();;
        List<Menu.MenuItem> breakfastItems = new ArrayList<>();
        List<Menu.MenuItem> starterItems = new ArrayList<>();;
        List<Menu.MenuItem> secondItems = new ArrayList<>();;
        List<Menu.MenuItem> drinkingItems = new ArrayList<>();;

        for (int i = 0; i < productDtos.size(); i++) {
            byte[] imageBytes = productDtos.get(i).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            menuItems.add(new Menu.MenuItem(i, bitmap, this.productDtos.get(i).getName(), (int) productDtos.get(i).getPrice() + "₽", productDtos.get(i).getDescription(), 1));
        }
        for (int i = 0; i < breakfastDtos.size(); i++) {
            byte[] imageBytes = breakfastDtos.get(i).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            breakfastItems.add(new Menu.MenuItem(i, bitmap, this.breakfastDtos.get(i).getName(), (int) breakfastDtos.get(i).getPrice() + "₽", breakfastDtos.get(i).getDescription(), 1));
        }
        for (int i = 0; i < starterDtos.size(); i++) {
            byte[] imageBytes = starterDtos.get(i).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            starterItems.add(new Menu.MenuItem(i, bitmap, this.starterDtos.get(i).getName(), (int) starterDtos.get(i).getPrice() + "₽", starterDtos.get(i).getDescription(), 1));
        }
        for (int i = 0; i < secondDtos.size(); i++) {
            byte[] imageBytes = secondDtos.get(i).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            secondItems.add(new Menu.MenuItem(i, bitmap, this.secondDtos.get(i).getName(), (int) secondDtos.get(i).getPrice() + "₽", secondDtos.get(i).getDescription(), 1));
        }
        for (int i = 0; i < drinkingDtos.size(); i++) {
            byte[] imageBytes = drinkingDtos.get(i).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            drinkingItems.add(new Menu.MenuItem(i, bitmap, this.drinkingDtos.get(i).getName(), (int) drinkingDtos.get(i).getPrice() + "₽", drinkingDtos.get(i).getDescription(), 1));
        }

        RecyclerView rvNovelties = view.findViewById(R.id.rvNovelties);
        RecyclerView rvPopular = view.findViewById(R.id.rvPopular);
        RecyclerView rvBreakfasts = view.findViewById(R.id.rvBreakfasts);
        RecyclerView rvStarters = view.findViewById(R.id.rvStarters);
        RecyclerView rvSeconds = view.findViewById(R.id.rvSeconds);

        Context context = getContext();

        rvNovelties.setAdapter(new MenuAdapter(context, menuItems));
        rvPopular.setAdapter(new MenuAdapter(context, drinkingItems));
        rvBreakfasts.setAdapter(new MenuAdapter(context, breakfastItems));
        rvStarters.setAdapter(new MenuAdapter(context, starterItems));
        rvSeconds.setAdapter(new MenuAdapter(context, secondItems));

        HorizontalScrollView horizontalScrollView = view.findViewById(R.id.horizontal_menu);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);

        final Button noveltiesButton = view.findViewById(R.id.novelties);
        final Button popularButton = view.findViewById(R.id.popular);
        final Button breakfastsButton = view.findViewById(R.id.breakfasts);
        final Button startersButton = view.findViewById(R.id.starters);
        final Button secondsButton = view.findViewById(R.id.seconds);

        noveltiesButton.setSelected(true);
        popularButton.setSelected(false);
        breakfastsButton.setSelected(false);
        startersButton.setSelected(false);
        secondsButton.setSelected(false);

        NestedScrollView scrollView = view.findViewById(R.id.svMenu);

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            Rect offsetViewBounds = new Rect();

            int noveltiesLastChildTop = getLastChildRelativeTop(v, rvNovelties, offsetViewBounds);
            int popularLastChildTop = getLastChildRelativeTop(v, rvPopular, offsetViewBounds);
            int breakfastsLastChildTop = getLastChildRelativeTop(v, rvBreakfasts, offsetViewBounds);
            int startersLastChildTop = getLastChildRelativeTop(v, rvStarters, offsetViewBounds);

            if(scrollY < noveltiesLastChildTop) {
                horizontalScrollView.smoothScrollTo(0, 0);
                noveltiesButton.setSelected(true);
                popularButton.setSelected(false);
                breakfastsButton.setSelected(false);
                startersButton.setSelected(false);
                secondsButton.setSelected(false);
            } else if(popularLastChildTop > scrollY) {
                horizontalScrollView.smoothScrollTo(0, 0);
                popularButton.setSelected(true);
                noveltiesButton.setSelected(false);
                breakfastsButton.setSelected(false);
                startersButton.setSelected(false);
                secondsButton.setSelected(false);
            } else if(breakfastsLastChildTop > scrollY) {
                horizontalScrollView.smoothScrollTo(breakfastsButton.getLeft() + breakfastsButton.getWidth() / 2 - getDisplayWidth() / 2, 0);
                breakfastsButton.setSelected(true);
                noveltiesButton.setSelected(false);
                popularButton.setSelected(false);
                startersButton.setSelected(false);
                secondsButton.setSelected(false);
            } else if(startersLastChildTop > scrollY) {
                horizontalScrollView.smoothScrollTo(horizontalScrollView.getWidth(), 0);
                startersButton.setSelected(true);
                noveltiesButton.setSelected(false);
                breakfastsButton.setSelected(false);
                popularButton.setSelected(false);
                secondsButton.setSelected(false);
            } else {
                horizontalScrollView.smoothScrollTo(horizontalScrollView.getWidth(), 0);
                secondsButton.setSelected(true);
                noveltiesButton.setSelected(false);
                breakfastsButton.setSelected(false);
                popularButton.setSelected(false);
                startersButton.setSelected(false);
            }
        });

        noveltiesButton.setOnClickListener(v -> {
            int titleTop = view.findViewById(R.id.novelties_title).getTop();
            scrollView.smoothScrollTo(0, titleTop);
        });

        popularButton.setOnClickListener(v -> {
            ((AppBarLayout) view.findViewById(R.id.appBar)).setExpanded(false, false);
            int titleTop = view.findViewById(R.id.popular_title).getTop();
            scrollView.smoothScrollTo(0, titleTop);
        });

        breakfastsButton.setOnClickListener(v -> {
            ((AppBarLayout) view.findViewById(R.id.appBar)).setExpanded(false, false);
            int titleTop = view.findViewById(R.id.breakfasts_title).getTop();
            scrollView.smoothScrollTo(0, titleTop);
        });

        startersButton.setOnClickListener(v -> {
            ((AppBarLayout) view.findViewById(R.id.appBar)).setExpanded(false, false);
            int titleTop = view.findViewById(R.id.starters_title).getTop();
            scrollView.smoothScrollTo(0, titleTop);
        });

        secondsButton.setOnClickListener(v -> {
            ((AppBarLayout) view.findViewById(R.id.appBar)).setExpanded(false, false);
            int titleTop = view.findViewById(R.id.seconds_title).getTop();
            scrollView.smoothScrollTo(0, titleTop);
        });
    }

    private int getDisplayWidth() {
        final WindowMetrics metrics = Objects.requireNonNull(getActivity()).getWindowManager().getCurrentWindowMetrics();
        return metrics.getBounds().width();
    }

    private int getLastChildRelativeTop(NestedScrollView scrollView, RecyclerView recyclerView, Rect offsetViewBounds) {
        View noveltiesLastChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        noveltiesLastChild.getDrawingRect(offsetViewBounds);
        scrollView.offsetRectIntoDescendantCoords(noveltiesLastChild, offsetViewBounds);
        return Math.abs(offsetViewBounds.top);
    }

    private ArrayList<ProductDto> getProductFromServer(String urlApiPart) throws MalformedURLException {
        ArrayList<ProductDto> productDtosFromServer = new ArrayList<>();
        String IP = "192.168.0.5:8080";
        int page = 0;
        URL producturl;
        while (true) {
            producturl = new URL(new URL("http", IP, urlApiPart + page).toString().replace("[", "").replace("]", ""));
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
                            productDtosFromServer.add(new ProductDto(jsonarray.getJSONObject(i).get("name").toString(), (double) jsonarray.getJSONObject(i).get("price"),
                                    (Boolean) jsonarray.getJSONObject(i).get("available"), jsonarray.getJSONObject(i).get("description").toString(),
                                    jsonarray.getJSONObject(i).get("type").toString(), jsonarray.getJSONObject(i).get("image").toString(),
                                    jsonarray.getJSONObject(i).get("nameBuilding").toString()));
                        }
                    } finally {
                        connection.disconnect();
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            page++;
        }
        System.out.println("finished downloading data by " + urlApiPart);
        return productDtosFromServer;
    }
}