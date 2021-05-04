package com.seminar.easyCookWeb.model.cost;

import com.seminar.easyCookWeb.pojo.cost.ProductItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("設定人工計價model")
public class HandmadeModel {
    private Long id;

    @ApiModelProperty(value = "人工價格", example = "50.0")
    private double cost;

    @ApiModelProperty(value = "人工價格策略名稱", example = "高單價產品")
    private String name;

    @Builder.Default
    @ApiModelProperty(value = "套用範圍")
    private List<ProductItemModel> products = new LinkedList<>();
}
