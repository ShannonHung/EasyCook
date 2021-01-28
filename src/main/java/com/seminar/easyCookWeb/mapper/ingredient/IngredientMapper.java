package com.seminar.easyCookWeb.mapper.ingredient;

import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import org.mapstruct.*;

import javax.persistence.ManyToOne;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    //因為我id是自己產生的所以我ignore targe's id
    @Mapping(target = "id",ignore = true)
    Ingredient toPOJO(IngredientModel ingredientModel);

    IngredientModel toModel(Ingredient ingredient);

    Iterable<IngredientModel> toIterableModel(Iterable<Ingredient> ingredients);
}
