package com.example.course_project.data.model;

import com.example.course_project.data.db.cart.CartItem;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;

@Setter
public class Order {
    private String clientName;
    private ArrayList<CartItem> orderContent;
    private LocalTime orderCompleteExpectedTime;
}
