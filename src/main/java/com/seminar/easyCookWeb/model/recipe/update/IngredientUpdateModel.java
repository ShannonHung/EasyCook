package com.seminar.easyCookWeb.model.recipe.update;

import com.seminar.easyCookWeb.pojo.ingredient.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Ingredient請求Entity樣式")
public class IngredientUpdateModel {
    private Long id;
}
