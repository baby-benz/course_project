package com.example.course_project.eventbus;

import com.example.course_project.data.db.cart.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateItemInCart {
    private CartItem cartItem;
}
