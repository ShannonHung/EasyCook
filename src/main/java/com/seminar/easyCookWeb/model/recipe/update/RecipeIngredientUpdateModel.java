package com.seminar.easyCookWeb.model.recipe.update;

import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientUpdateModel {
    private long id;

    private IngredientUpdateModel ingredient;

    private Double quantityRequired = 0D;
}
