package com.seminar.easyCookWeb.mapper.recipe;

import com.seminar.easyCookWeb.model.recipe.RecipeIngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {
    RecipeIngredientMapper MAPPER = Mappers.getMapper( RecipeIngredientMapper.class );

    RecipeIngredientModel toModel(RecipeIngredient recipeIngredient);

    RecipeIngredient toPOJO(RecipeIngredientModel recipeIngredientModel);

    List<RecipeIngredientModel> toModels(List<RecipeIngredient> recipeIngredients);
}
