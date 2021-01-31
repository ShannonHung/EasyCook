package com.seminar.easyCookWeb.mapper.recipe;

import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RecipeStepMapper.class, RecipeIngredientMapper.class})
public interface RecipeMapper {
    RecipeMapper MAPPER = Mappers.getMapper( RecipeMapper.class );

    RecipeModel toModel(Recipe recipe);

    Recipe toPOJO(RecipeModel recipeModel);

    List<RecipeModel> toModels(List<Recipe> recipes);
}
