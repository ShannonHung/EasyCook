package com.seminar.easyCookWeb.model.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@ApiModel("進貨-食材 PurchaseIngredientModel請求Entity樣式")
public class PurchaseIngredientModel {
    @ApiModelProperty(value = "進貨編號", required = true)
    private long iid;

    @ApiModelProperty(value = "有效日期" , example = "2021-03-20T22:00+08:00", required = true)
    private OffsetDateTime date;

    @ApiModelProperty(value = "進貨數量" , example = "50")
    private Double quantity;

    @ApiModelProperty(value = "進貨單價" , example = "30")
    private Double price;

    @ApiModelProperty(value = "進貨小計" , example = "150")
    private Double sum;
}
