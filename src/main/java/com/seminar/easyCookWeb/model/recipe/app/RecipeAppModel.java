package com.seminar.easyCookWeb.model.recipe.app;
import com.seminar.easyCookWeb.model.recipe.RecipeIngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("app用於首頁的recipe model")
public class RecipeAppModel {
    private long id;

    @NotNull
    @ApiModelProperty(value = "食譜名稱", example = "三明治", required = true)
    private String name;

    @Builder.Default
    @ApiModelProperty(value = "食譜版本", example = "NORMAL", required = true)
    private String version = "NORMAL";


    @ApiModelProperty(value = "食譜按讚次數", example = "10")
    private int likesCount;

    @ApiModelProperty(value = "食譜價格", example = "10.5")
    @Builder.Default
    private double price = 0D;

    @ApiModelProperty(value = "食譜相片連結")
    private String photo;

    @ApiModelProperty(value = "forBlob ImageId")
    private String blobId;
}
