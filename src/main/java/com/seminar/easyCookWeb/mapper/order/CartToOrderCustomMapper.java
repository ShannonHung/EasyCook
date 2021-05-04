package com.seminar.easyCookWeb.mapper.order;

import com.seminar.easyCookWeb.model.cart.response.CartRecipeCustomModel;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import com.seminar.easyCookWeb.pojo.order.OrderItem;
import com.seminar.easyCookWeb.pojo.order.OrderItemCustom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartToOrderCustomMapper {

    @Mapping(target = "ingredientName", source = "ingredient.name")
    @Mapping(target = "ingredientPrice", source = "ingredient.price")
    OrderItemCustom cartCusToOrderCus(CartRecipeCustomModel cartCustom);

//    @Mapping(target = "itemPrice", source = "sum")
//    OrderItem carToOrder(CartRecipeModel cart);
}
