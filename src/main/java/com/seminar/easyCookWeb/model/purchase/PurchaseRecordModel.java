package com.seminar.easyCookWeb.model.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("進貨紀錄PurchaseRecordModel 請求Entity樣式")
public class PurchaseRecordModel {
    @ApiModelProperty(value = "進貨編號", required = true)
    private long iid;

    //其他關連的FK可依照API需求放置
//    @NotNull
    @ApiModelProperty(value = "供應商id", example = "6")
    private Long supplierId;

    @ApiModelProperty(value = "進貨日期" , example = "2021-03-20T22:00+08:00", required = true)
    private OffsetDateTime date;

    @ApiModelProperty(value = "折扣" , example = "500")
    private Double discount;

    @ApiModelProperty(value = "總價" , example = "3000")
    private Double total;

}
