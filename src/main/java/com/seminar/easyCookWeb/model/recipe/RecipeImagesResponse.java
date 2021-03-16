package com.seminar.easyCookWeb.model.recipe;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Recipe請求Entity樣式")
public class RecipeImagesResponse {

    @ApiModelProperty(value = "相片id", example = "1")
    private Long id;

    @ApiModelProperty(value = "相片名稱", example = "三明治.png")
    private String name;

    @ApiModelProperty(value = "相片content type", example = "image/png")
    private String type;

    @ApiModelProperty(value = "相片內容", example = "Ajkdgl9u029384019k1jlkj3 (bytes)")
    private byte[] picByte;

}
