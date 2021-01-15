package com.example.course_project.data.db.cart;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

public interface CartDataSource {
    Flowable<List<CartItem>> getAllCart(String userId);

    Single<Long> sumPriceInCart(long userId);

    Single<CartItem> getCartItem(long userId, long productId);

    Completable insertOrReplaceAll(CartItem... cartItem);

    Single<Integer> updateCart(CartItem cartItem);

    Single<Integer> removeCartItem(CartItem cartItem);

    Single<Integer> clearCart(long userId);
}
