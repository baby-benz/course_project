package com.example.course_project.data.db.cart;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

public interface CartDataSource {
    Flowable<List<CartItem>> getAllCart(String userId);

    Single<Double> sumPriceInCart(String userId);

    Single<CartItem> getCartItem(String userId, long productId);

    Completable insertOrReplaceAll(CartItem... cartItem);

    Single<Integer> updateCart(CartItem cartItem);

    Single<Integer> removeCartItem(CartItem cartItem);

    Single<Integer> clearCart(String userId);
}
