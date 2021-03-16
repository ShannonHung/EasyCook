package com.seminar.easyCookWeb.mapper.recipe;

import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RecipeStepMapper.class, RecipeIngredientMapper.class})
public interface RecipeMapper {
    RecipeModel toModel(Recipe recipe);

    //因為service那邊會先存一次，在存之前要確保step是空的不然會先把step重複存進去
    @Mapping(target = "recipeSteps", ignore = true)
    @Mapping(target = "recipeIngredients", ignore = true)
    Recipe toPOJO(RecipeModel recipeModel);

    List<RecipeModel> toModels(List<Recipe> recipes);

    Iterable<RecipeModel> toIterableModel(Iterable<Recipe> recipes);

    @Mapping(target = "id",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(RecipeModel recipeModel, @MappingTarget Recipe recipe);

}
