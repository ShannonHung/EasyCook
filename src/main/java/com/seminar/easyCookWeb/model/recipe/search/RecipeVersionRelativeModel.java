package com.seminar.easyCookWeb.model.recipe.search;

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
@ApiModel("該食譜其他version樣式")
public class RecipeVersionRelativeModel {
    @ApiModelProperty(value = "食譜版本", example = "NORMAL")
    private String version;

    @ApiModelProperty(value = "食譜id", example = "2")
    private Long recipeId;
}
