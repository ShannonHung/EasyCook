package com.seminar.easyCookWeb.mapper.ingredient;

import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import org.mapstruct.*;

import javax.persistence.ManyToOne;
import java.util.Iterator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toPOJO(IngredientModel ingredientModel);

    IngredientModel toModel(Ingredient ingredient);

    Iterable<IngredientModel> toIterableModel(Iterable<Ingredient> ingredients);

    @Mapping(target = "id",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(IngredientModel ingredientModel, @MappingTarget Ingredient ingredient);


}
