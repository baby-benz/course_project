package com.example.course_project.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.course_project.R;
import com.example.course_project.data.db.cart.CartItem;
import com.example.course_project.event.UpdateItemInCart;
import lombok.RequiredArgsConstructor;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

@RequiredArgsConstructor
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context context;
    private final List<CartItem> items;

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_cart_item, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = items.get(position);

        //holder.imageView.setImageBitmap(cartItem.getProductImage());
        holder.nameTextView.setText(cartItem.getProductName());

        int productCount = cartItem.getCount();
        holder.priceTextView.setText(String.valueOf(cartItem.getProductPrice() * productCount));
        holder.countNumberButton.setNumber(String.valueOf(productCount));

        holder.countNumberButton.setOnValueChangeListener((view, oldValue, newValue) -> {
            cartItem.setCount(newValue);
            EventBus.getDefault().postSticky(new UpdateItemInCart(cartItem));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView nameTextView;
        public final TextView priceTextView;
        public final ElegantNumberButton countNumberButton;

        public ViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.item_image);
            nameTextView = view.findViewById(R.id.item_name);
            priceTextView = view.findViewById(R.id.item_price);
            countNumberButton = view.findViewById(R.id.item_count);
        }
    }
}
