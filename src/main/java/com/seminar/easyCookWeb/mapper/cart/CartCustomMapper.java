package com.seminar.easyCookWeb.mapper.cart;

import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeCustomizeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeCustomModel;
import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface CartCustomMapper {
    CartRecipeCustomModel toModel(CartRecipeCustomize customize);

    CartRecipeCustomize toPojo(CartRecipeCustomModel customizeModel);

    CartRecipeCustomize toPojo(CartRecipeCustomizeRequest request);
}
