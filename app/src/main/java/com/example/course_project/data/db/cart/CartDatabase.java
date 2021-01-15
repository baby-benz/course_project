package com.example.course_project.data.db.cart;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CartItem.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDAO cartDao();

    public static CartDatabase instance;

    public static CartDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, CartDatabase.class, "CanteenDB1").build();
        }
        return instance;
    }
}

