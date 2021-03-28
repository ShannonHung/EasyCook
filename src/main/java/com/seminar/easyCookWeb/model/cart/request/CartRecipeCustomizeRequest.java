package com.seminar.easyCookWeb.model.cart.request;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("購物車食材請求Entity樣式")
public class CartRecipeCustomizeRequest {
    private Long id;

    @ApiModelProperty(value = "食材Id", example = "1")
    private Long ingredientId;

    @ApiModelProperty(value = "食材數量", example = "5")
    private Double quantityRequired = 0D;
}
