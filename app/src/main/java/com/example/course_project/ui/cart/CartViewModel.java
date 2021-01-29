package com.example.course_project.ui.cart;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.course_project.data.db.cart.CartDataSource;
import com.example.course_project.data.db.cart.CartDatabase;
import com.example.course_project.data.db.cart.CartItem;
import com.example.course_project.data.db.cart.LocalCartDataSource;
import com.example.course_project.data.model.Common;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CartViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;
    private MutableLiveData<List<CartItem>> mutableLiveDataCartItems;

    public void initCartDataSource(Context context) {
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDao());
    }

    public void onStop() {
        compositeDisposable.clear();
    }

    public MutableLiveData<List<CartItem>> getMutableLiveDataCartItems() {
        if (mutableLiveDataCartItems == null) {
            mutableLiveDataCartItems = new MutableLiveData<>();
        }
        getAllCartItems();
        return mutableLiveDataCartItems;
    }

    public void getAllCartItems() {
        compositeDisposable.add(cartDataSource.getAllCart(Common.LOGGED_IN_USER != null ? Common.LOGGED_IN_USER.getUserId() : "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    mutableLiveDataCartItems.setValue(cartItems);
                }, throwable -> mutableLiveDataCartItems.setValue(null))
        );
    }
}
