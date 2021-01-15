package com.example.course_project.data.db.cart;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LocalCartDataSource implements CartDataSource {
    private final CartDAO cartDAO;

    @Override
    public Flowable<List<CartItem>> getAllCart(String userId) {
        return cartDAO.getAllCart(userId);
    }

    @Override
    public Single<Long> sumPriceInCart(long userId) {
        return cartDAO.sumPriceInCart(userId);
    }

    @Override
    public Single<CartItem> getCartItem(long userId, long productId) {
        return cartDAO.getCartItem(userId, productId);
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItem) {
        return cartDAO.insertOrReplaceAll(cartItem);
    }

    @Override
    public Single<Integer> updateCart(CartItem cartItem) {
        return cartDAO.updateCart(cartItem);
    }

    @Override
    public Single<Integer> removeCartItem(CartItem cartItem) {
        return cartDAO.updateCart(cartItem);
    }

    @Override
    public Single<Integer> clearCart(long userId) {
        return cartDAO.clearCart(userId);
    }
}
