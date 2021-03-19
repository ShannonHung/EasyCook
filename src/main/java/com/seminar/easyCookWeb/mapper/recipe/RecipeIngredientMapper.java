package com.seminar.easyCookWeb.mapper.recipe;

import com.seminar.easyCookWeb.model.recipe.RecipeIngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.model.recipe.update.RecipeIngredientUpdateModel;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {
    RecipeIngredientMapper MAPPER = Mappers.getMapper( RecipeIngredientMapper.class );

    RecipeIngredientModel toModel(RecipeIngredient recipeIngredient);

    RecipeIngredient toPOJO(RecipeIngredientModel recipeIngredientModel);

    RecipeIngredient toPOJO(RecipeIngredientUpdateModel recipeIngredientUpdateModel);

    List<RecipeIngredientModel> toModels(List<RecipeIngredient> recipeIngredients);

    Iterable<RecipeIngredientModel> toIterableModel(Iterable<RecipeIngredient> recipes);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "ingredient",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(RecipeIngredientModel recipeIngredientModel, @MappingTarget RecipeIngredient recipeIngredient);
}
