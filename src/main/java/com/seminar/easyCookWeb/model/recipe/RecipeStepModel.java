package com.seminar.easyCookWeb.model.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Null;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStepModel {
    private long id;

    @ApiModelProperty(value = "食譜步驟的影片時間點", example = "0:47")
    private String startTime;
    @ApiModelProperty(value = "食譜步驟說明", example = "撒鹽巴&黑胡椒")
    private String note;

}
