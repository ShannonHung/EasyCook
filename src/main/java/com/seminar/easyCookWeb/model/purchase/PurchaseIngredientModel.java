package com.seminar.easyCookWeb.model.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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

    //其他FK以JSON傳入時要寫
    //    @NotNull
    @ApiModelProperty(value = "食材id", example = "6")
    private Long ingredientId;

    @ApiModelProperty(value = "進貨紀錄id", example = "1")
    private Long recordId;
}
