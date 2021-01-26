package com.example.course_project.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.course_project.data.model.MenuItem;
import com.example.course_project.eventbus.CounterCartEvent;
import com.example.course_project.R;
import com.example.course_project.data.db.cart.CartDataSource;
import com.example.course_project.data.db.cart.CartDatabase;
import com.example.course_project.data.db.cart.CartItem;
import com.example.course_project.data.db.cart.LocalCartDataSource;
import com.example.course_project.data.model.Common;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MenuItem}.
 */
@RequiredArgsConstructor
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private final Context context;
    private final List<MenuItem> items;
    private final CompositeDisposable compositeDisposable;
    private final CartDataSource cartDataSource;

    public MenuAdapter(Context context, List<MenuItem> items) {
        this.context = context;
        this.items = items;
        this.compositeDisposable = new CompositeDisposable();
        this.cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDao());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, int position) {
        MenuItem menuItem = items.get(position);

        /*holder.imageView.setImageBitmap(menuItem.getImage());*/
        holder.nameTextView.setText(menuItem.getName());
        holder.priceTextView.setText(menuItem.getPrice());
        holder.descriptionTextView.setText(menuItem.getDescription());

        holder.toCartButton.setOnClickListener(view -> {
            CartItem cartItem = new CartItem();

            cartItem.setUserId(Common.LOGGED_IN_USER.getUserId());
            cartItem.setProductId(menuItem.getId());
            cartItem.setProductName(menuItem.getName());
            cartItem.setCount(menuItem.getCount());

            String priceWithCurrency = menuItem.getPrice();
            cartItem.setProductPrice(Double.valueOf(priceWithCurrency.substring(0, priceWithCurrency.length() - 1)));

            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().postSticky(new CounterCartEvent(true));
                    }, throwable -> {
                        Toast.makeText(context, "[ОШИБКА ДОБАВЛЕНИЯ В КОРЗИНУ]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }));
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
        public final TextView descriptionTextView;
        public final Button toCartButton;

        public ViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.item_image);
            nameTextView = view.findViewById(R.id.item_name);
            priceTextView = view.findViewById(R.id.item_price);
            descriptionTextView = view.findViewById(R.id.item_description);
            toCartButton = view.findViewById(R.id.to_cart);
        }
    }
}