package com.seminar.easyCookWeb.mapper.order;

import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeCustomizeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeCustomModel;
import com.seminar.easyCookWeb.model.order.OrderItemCustomModel;
import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import com.seminar.easyCookWeb.pojo.order.OrderItemCustom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface OrderItemCustomMapper {
    OrderItemCustomModel toModel(OrderItemCustom customize);

    OrderItemCustom toPojo(OrderItemCustomModel customizeModel);
}
