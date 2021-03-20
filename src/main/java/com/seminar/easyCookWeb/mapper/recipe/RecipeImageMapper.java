package com.seminar.easyCookWeb.mapper.recipe;

import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.model.recipe.RecipeIngredientModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeImageMapper {
    RecipeImageMapper MAPPER = Mappers.getMapper( RecipeImageMapper.class );


    @Mapping(target = "size", ignore = true)
    RecipeImageModel toModel(RecipeImage recipeImage);

    @Mapping(target = "picByte", ignore = true)
    RecipeImage toPOJO(RecipeImageModel recipeImageModel);

    List<RecipeImageModel> toModels(List<RecipeImage> recipeImages);
}
