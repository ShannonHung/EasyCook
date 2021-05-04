package com.seminar.easyCookWeb.mapper.cart;

import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.mapper.user.MemberMapper;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.recipe.update.RecipeUpdateModel;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {MemberMapper.class, RecipeMapper.class, CartCustomMapper.class})
public interface CartMapper {
    CartRecipeModel toModel(CartRecipe cartRecipe);

    CartRecipe toPojo(CartRecipeModel cartRecipeModel);

    Iterable<CartRecipeModel> toIterableModel(Iterable<CartRecipe> cartRecipes);

    @Mapping(target = "customize", source = "recipeIngredients")
    @Mapping(target = "id",ignore = true)
    CartRecipe DefaultRecipeToCart(Recipe recipe);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "isCustomize",ignore = true)
    @Mapping(target = "member",ignore = true)
    @Mapping(target = "recipe",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(CartRecipeModel oldPojo, @MappingTarget CartRecipe newPojo);

}
