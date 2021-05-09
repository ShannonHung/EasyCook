package com.seminar.easyCookWeb.model.order.update;

import com.seminar.easyCookWeb.model.order.OrderItemModel;
import com.seminar.easyCookWeb.model.user.MemberResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("訂單更新 OrderForm 請求Entity樣式")
public class OrderUpdateEmployee {

    private long id;

    @ApiModelProperty(value = "下訂時間點")
    private OffsetDateTime orderTime;

    @ApiModelProperty(value = "付款方式")
    private String payWay;

    @ApiModelProperty(value = "運送方式")
    private String serviceWay;

    @ApiModelProperty(value = "希望抵達時間")
    private OffsetDateTime hopeDeliverTime;

    @ApiModelProperty(value = "實際抵達時間")
    private OffsetDateTime realDeliverTime;

    @ApiModelProperty(value = "出貨時間")
    private OffsetDateTime shippingTime;

    @ApiModelProperty(value = "訂單狀態", example = "toConfirm")
    @Builder.Default
    private String status = "toConfirm";

    @ApiModelProperty(value = "地址", example = "台北市大安區基隆路四段")
    private String address;

    @ApiModelProperty(value = "運費", example = "60")
    private Double transportFee = 0D;

    @ApiModelProperty(value = "折扣", example = "160")
    private Double discount = 0D;

    @ApiModelProperty(value = "總額", example = "250")
    private Double sum = 0D;
}
