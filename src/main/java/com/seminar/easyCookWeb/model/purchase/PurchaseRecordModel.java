package com.seminar.easyCookWeb.model.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.time.OffsetDateTime;


@Data
@ApiModel("進貨紀錄PurchaseRecordModel請求Entity樣式")
public class PurchaseRecordModel {
    @ApiModelProperty(value = "進貨編號", required = true)
    private long iid;

    //其他關連的FK都不用做

    @ApiModelProperty(value = "進貨日期" , example = "2021-03-20T22:00+08:00", required = true)
    @NotBlank
    private OffsetDateTime date;

    @ApiModelProperty(value = "折扣" , example = "500", required = true)
    private Double discount;

    @ApiModelProperty(value = "總價" , example = "3000", required = true)
    private Double total;

}
