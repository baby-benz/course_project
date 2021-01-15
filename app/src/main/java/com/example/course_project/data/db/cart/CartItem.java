package com.example.course_project.data.db.cart;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(tableName="Cart")
public class CartItem {
    @ColumnInfo(name = "user_id")
    private String userId;

    @PrimaryKey
    @ColumnInfo(name="product_id")
    private long productId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "product_price")
    private Double productPrice;

    @ColumnInfo
    private int count;
}
