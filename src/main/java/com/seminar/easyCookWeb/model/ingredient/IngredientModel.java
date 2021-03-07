package com.seminar.easyCookWeb.model.ingredient;

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
public class IngredientModel {
    private Long id;

    @ApiModelProperty(value = "食材名稱", example = "中國水梨", required = true)
    private String name;

    @ApiModelProperty(value = "食材種類", example = "VEGETABLE", required = true)
    private Category category = Category.OTHER;

    @ApiModelProperty(value = "食材單位", example = "顆", required = true)
    private String unit;

    @ApiModelProperty(value = "食材熱量", example = "100.567", required = true)
    private Double kcal;

    @ApiModelProperty(value = "食材產地", example = "TAIWAN", required = true)
    private String country;

    @ApiModelProperty(value = "食材區域", example = "TAIPEI", required = true)
    private String city;

    @ApiModelProperty(value = "食材價格", example = "100", required = true)
    @Builder.Default
    private int price = 0;

    @ApiModelProperty(value = "食材安全庫存", example = "10.00", required = true)
    @Builder.Default
    private Double safetyStock = 0D;

    @ApiModelProperty(value = "食材目前庫存", example = "100.00", required = true)
    @Builder.Default
    private Double stock = 0D;

}
