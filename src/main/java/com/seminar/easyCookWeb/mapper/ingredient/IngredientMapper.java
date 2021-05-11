package com.seminar.easyCookWeb.mapper.ingredient;

import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toPOJO(IngredientModel ingredientModel);

    IngredientModel toModel(Ingredient ingredient);

    Iterable<IngredientModel> toIterableModel(Iterable<Ingredient> ingredients);

    @Mapping(target = "id",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(IngredientModel ingredientModel, @MappingTarget Ingredient ingredient);


}
