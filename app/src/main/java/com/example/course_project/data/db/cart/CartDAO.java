package com.example.course_project.data.db.cart;

import androidx.room.*;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart WHERE user_id=:userId")
    Flowable<List<CartItem>> getAllCart(String userId);

    @Query("SELECT SUM(product_price*count) FROM Cart WHERE user_id=:userId")
    Single<Double> sumPriceInCart(long userId);

    @Query("SELECT * FROM Cart WHERE user_id=:userId AND product_id=:productId")
    Single<CartItem> getCartItem(long userId, long productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrReplaceAll(CartItem... cartItem);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Single<Integer> updateCart(CartItem cartItem);

    @Delete
    Single<Integer> removeCartItem(CartItem cartItem);

    @Query("DELETE FROM Cart WHERE user_id=:userId")
    Single<Integer> clearCart(long userId);
}
