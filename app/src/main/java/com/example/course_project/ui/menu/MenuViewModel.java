package com.example.course_project.ui.menu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.lifecycle.ViewModel;
import com.example.course_project.data.model.MenuItem;
import com.example.course_project.dto.ProductDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

public class MenuViewModel extends ViewModel {
    volatile ArrayList<ProductDto> noveltiesDtos = new ArrayList<>();
    volatile ArrayList<ProductDto> breakfastDtos = new ArrayList<>();
    volatile ArrayList<ProductDto> starterDtos = new ArrayList<>();
    volatile ArrayList<ProductDto> secondDtos = new ArrayList<>();
    volatile ArrayList<ProductDto> drinkingDtos = new ArrayList<>();

    private final String noveltiesUrlPart = "/api/product/";
    private final String breakfastsUrlPart = "/api/product/type/breakfast/";
    private final String startersUrlPart = "/api/product/type/starter/";
    private final String secondsUrlPart = "/api/product/type/second/";
    private final String drinkingUrlPart = "/api/product/type/drinking/";

    public ArrayList<ProductDto> getProductFromServer(String urlApiPart) throws MalformedURLException {
        ArrayList<ProductDto> productDtosFromServer = new ArrayList<>();
        String IP = "192.168.0.5:8080";
        int page = 0;
        URL producturl;
        while (true) {
            producturl = new URL(new URL("http", IP, urlApiPart + page).toString().replace("[", "").replace("]", ""));
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

    public List<MenuItem> getNoveltiesItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        new Thread(() -> {
            try {
                noveltiesDtos = getProductFromServer(noveltiesUrlPart);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < noveltiesDtos.size(); i++) {
                byte[] imageBytes = noveltiesDtos.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                menuItems.add(new MenuItem(i, bitmap, this.noveltiesDtos.get(i).getName(), (int) noveltiesDtos.get(i).getPrice() + "₽", noveltiesDtos.get(i).getDescription()));
            }
        }).start();

        return menuItems;
    }

    public List<MenuItem> getBreakfastItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        new Thread(() -> {
            try {
                breakfastDtos = getProductFromServer(breakfastsUrlPart);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < breakfastDtos.size(); i++) {
                byte[] imageBytes = breakfastDtos.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                menuItems.add(new MenuItem(i, bitmap, this.breakfastDtos.get(i).getName(), (int) breakfastDtos.get(i).getPrice() + "₽", breakfastDtos.get(i).getDescription()));
            }
        }).start();

        return menuItems;
    }

    public List<MenuItem> getStarterItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        new Thread(() -> {
            try {
                starterDtos = getProductFromServer(startersUrlPart);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < starterDtos.size(); i++) {
                byte[] imageBytes = starterDtos.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                menuItems.add(new MenuItem(i, bitmap, this.starterDtos.get(i).getName(), (int) starterDtos.get(i).getPrice() + "₽", starterDtos.get(i).getDescription()));
            }
        }).start();

        return menuItems;
    }

    public List<MenuItem> getSecondItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        new Thread(() -> {
            try {
                secondDtos = getProductFromServer(secondsUrlPart);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < secondDtos.size(); i++) {
                byte[] imageBytes = secondDtos.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                menuItems.add(new MenuItem(i, bitmap, this.secondDtos.get(i).getName(), (int) secondDtos.get(i).getPrice() + "₽", secondDtos.get(i).getDescription()));
            }
        }).start();

        return menuItems;
    }

    public List<MenuItem> getDrinkingItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        new Thread(() -> {
            try {
                drinkingDtos = getProductFromServer(drinkingUrlPart);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < drinkingDtos.size(); i++) {
                byte[] imageBytes = drinkingDtos.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                menuItems.add(new MenuItem(i, bitmap, this.drinkingDtos.get(i).getName(), (int) drinkingDtos.get(i).getPrice() + "₽", drinkingDtos.get(i).getDescription()));
            }
        }).start();

        while (noveltiesDtos.size() == 0
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

        return menuItems;
    }
}
