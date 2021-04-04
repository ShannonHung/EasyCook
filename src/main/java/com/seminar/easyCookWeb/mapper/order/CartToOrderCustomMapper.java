package com.seminar.easyCookWeb.mapper.order;

import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import com.seminar.easyCookWeb.pojo.order.OrderItemCustom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartToOrderCustomMapper {
    OrderItemCustom cartCusToOrderCus(CartRecipeCustomize cartCustom);
}
