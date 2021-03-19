package com.seminar.easyCookWeb.model.recipe.search;

import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("該食譜其他version樣式")
public class RecipeVersionModel {
    @ApiModelProperty(value = "目前的食譜")
    private RecipeModel currentRecipe;
    @ApiModelProperty(value = "目前食譜相關的版本")
    private List<RecipeVersionRelativeModel> existedVersions;
}
