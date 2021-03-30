package com.seminar.easyCookWeb.mapper.order;

import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.order.OrderFormModel;
import com.seminar.easyCookWeb.model.order.OrderItemModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import com.seminar.easyCookWeb.pojo.order.OrderItem;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RecipeMapper.class, OrderItemCustomMapper.class, CartToOrderCustomMapper.class})
public interface OrderItemMapper {

    OrderItemModel toModel(OrderItem orderItem);

    OrderItem toPOJO(OrderItemModel orderItemModel);

    OrderItem cartToOrder(CartRecipe cart);

    List<OrderItemModel> toModels(List<OrderItem> orderItems);

    List<OrderItem> carsToOrders(List<CartRecipe> carts);

}
