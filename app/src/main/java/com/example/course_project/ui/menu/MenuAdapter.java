package com.example.course_project.ui.menu;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.course_project.R;
import com.example.course_project.data.model.Menu.MenuItem;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MenuItem}.
 */
@RequiredArgsConstructor
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private final List<MenuItem> mItems;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, int position) {
        MenuItem menuItem = mItems.get(position);

        /*ImageView imageView = holder.imageView;
        imageView.setImageBitmap(menuItem.getImage());*/
        TextView nameTextView = holder.nameTextView;
        nameTextView.setText(menuItem.getName());
        TextView priceTextView = holder.priceTextView;
        priceTextView.setText(menuItem.getPrice());
        TextView descriptionTextView = holder.descriptionTextView;
        descriptionTextView.setText(menuItem.getDescription());
        ElegantNumberButton countNumberButton = holder.countNumberButton;
        countNumberButton.setNumber(String.valueOf(menuItem.getCount()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView nameTextView;
        public final TextView priceTextView;
        public final TextView descriptionTextView;
        public final ElegantNumberButton countNumberButton;

        public ViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.item_image);
            nameTextView = view.findViewById(R.id.item_name);
            priceTextView = view.findViewById(R.id.item_price);
            descriptionTextView = view.findViewById(R.id.item_description);
            countNumberButton = view.findViewById(R.id.item_count);
        }
    }
}