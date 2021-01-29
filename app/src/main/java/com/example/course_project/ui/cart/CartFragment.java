package com.example.course_project.ui.cart;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.course_project.R;
import com.example.course_project.data.db.cart.CartDataSource;
import com.example.course_project.data.db.cart.CartDatabase;
import com.example.course_project.data.db.cart.LocalCartDataSource;
import com.example.course_project.data.model.Common;
import com.example.course_project.event.UpdateItemInCart;
import com.example.course_project.ui.order.OrderDialogFragment;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private RecyclerView rvCart;
    private Parcelable recyclerViewState;
    private CartDataSource cartDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartViewModel cartViewModel = new ViewModelProvider(this, new CartViewModelFactory()).get(CartViewModel.class);

        cartViewModel.initCartDataSource(getContext());
        rvCart = view.findViewById(R.id.rvCart);
        cartViewModel.getMutableLiveDataCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            if (cartItems == null || cartItems.isEmpty()) {
                view.findViewById(R.id.finish_order).setVisibility(View.GONE);
                view.findViewById(R.id.cart_total).setVisibility(View.GONE);
                rvCart.setVisibility(View.GONE);
                view.findViewById(R.id.cart_empty).setVisibility(View.VISIBLE);
                view.findViewById(R.id.go_to_menu).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.cart_empty).setVisibility(View.GONE);
                rvCart.setVisibility(View.VISIBLE);
                rvCart.setAdapter(new CartAdapter(getContext(), cartItems));
                calculateTotalPrice();
            }
        });

        view.findViewById(R.id.finish_order).setOnClickListener(v -> {
            if (Common.LOGGED_IN_USER != null) {
                OrderDialogFragment orderDialogFragment = new OrderDialogFragment();
                orderDialogFragment.show(requireActivity().getSupportFragmentManager(), "");
            } else {
                Toast.makeText(getContext(), "Пожалуйста, авторизуйтесь", Toast.LENGTH_SHORT).show();
                requireActivity().findViewById(R.id.profile).performClick();
            }
        });

        view.findViewById(R.id.go_to_menu).setOnClickListener(v -> requireActivity().findViewById(R.id.menu).performClick());

        initViews();
    }

    private void initViews() {
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDao());
        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateItemInCartEvent(UpdateItemInCart event) {
        if (event.getCartItem() != null) {
            recyclerViewState = rvCart.getLayoutManager().onSaveInstanceState();
            cartDataSource.updateCart(event.getCartItem())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull Integer integer) {
                            calculateTotalPrice();
                            rvCart.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(getContext(), "[ОБНОВЛЕНИЕ КОРЗИНЫ]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void calculateTotalPrice() {
        cartDataSource.sumPriceInCart(Common.LOGGED_IN_USER != null ? Common.LOGGED_IN_USER.getUserId() : "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Double>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Double price) {
                        ((TextView) requireActivity().findViewById(R.id.cart_total_sum)).setText(String.valueOf(price));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getContext(), "[ИТОГОВАЯ СУММА]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}