package com.example.course_project.ui.menu;

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

        List<Menu.MenuItem> menuItems = Menu.ITEMS;

        RecyclerView rvNovelties = view.findViewById(R.id.rvNovelties);
        RecyclerView rvPopular = view.findViewById(R.id.rvPopular);
        RecyclerView rvBreakfasts = view.findViewById(R.id.rvBreakfasts);
        RecyclerView rvStarters = view.findViewById(R.id.rvStarters);
        RecyclerView rvSeconds = view.findViewById(R.id.rvSeconds);

        rvNovelties.setAdapter(new MenuAdapter(menuItems));
        rvPopular.setAdapter(new MenuAdapter(menuItems));
        rvBreakfasts.setAdapter(new MenuAdapter(menuItems));
        rvStarters.setAdapter(new MenuAdapter(menuItems));
        rvSeconds.setAdapter(new MenuAdapter(menuItems));

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