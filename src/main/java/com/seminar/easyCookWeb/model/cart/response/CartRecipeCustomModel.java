package com.seminar.easyCookWeb.model.cart.response;

import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
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
@ApiModel("購物車食材回應Entity樣式")
public class CartRecipeCustomModel {
    private Long id;

    @ApiModelProperty(value = "食材Id", example = "1")
    private IngredientModel ingredient;

    @ApiModelProperty(value = "食材數量", example = "5")
    private Double quantityRequired = 0D;
}
