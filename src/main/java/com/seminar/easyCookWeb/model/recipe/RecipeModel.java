package com.seminar.easyCookWeb.model.recipe;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Recipe請求Entity樣式")
public class RecipeModel {
    private long id;

    @NotNull
    @ApiModelProperty(value = "食譜名稱", example = "三明治", required = true)
    private String name;

    @Builder.Default
    @ApiModelProperty(value = "食譜版本", example = "NORMAL", required = true)
    private String version = "NORMAL";

    @ApiModelProperty(value = "食譜影片連結", example = "https://youtu.be/QDXk0SYyagg")
    private String link;

    @ApiModelProperty(value = "食譜按讚次數", example = "10")
    private int likesCount;

    @ApiModelProperty(value = "食譜描述", example = "使用高山高麗菜的青蔬炒五花，兼顧您的健康與味覺享受...")
    private String description;

    @ApiModelProperty(value = "食譜價格", example = "10.5")
    @Builder.Default
    private int price = 0;


    @Builder.Default
    @ApiModelProperty(value = "食譜相片")
    private List<RecipeImagesResponse> photos = new LinkedList<>();

    @Builder.Default
    @ApiModelProperty(value = "食譜步驟")
    private List<RecipeStepModel> recipeSteps = new LinkedList<>();

    @Builder.Default
    @ApiModelProperty(value = "食譜所需食材")
    private List<RecipeIngredientModel> recipeIngredients = new LinkedList<>();

}
