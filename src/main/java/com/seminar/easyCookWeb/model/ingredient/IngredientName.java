package com.seminar.easyCookWeb.model.ingredient;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class IngredientName {
    @ApiModelProperty(value = "食材搜尋名稱", example = "0:47")
    private String ingredientName;
}
