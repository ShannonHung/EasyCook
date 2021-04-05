package com.seminar.easyCookWeb.model.cost;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Entity;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("人工計價所套用的食譜")
public class ProductItemModel {
    @ApiModelProperty(value = "食譜id", example = "1")
    private long productId;
}
