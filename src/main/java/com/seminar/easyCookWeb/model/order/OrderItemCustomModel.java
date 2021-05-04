package com.seminar.easyCookWeb.model.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.order.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("訂單細項食材 model 請求Entity樣式")
public class OrderItemCustomModel {
    @ApiModelProperty(value = "訂單細項材料id")
    private long id;

    @ApiModelProperty(value = "訂單食材名稱")
    private String ingredientName;

    @ApiModelProperty(value = "訂單食材價格")
    @Builder.Default
    private double ingredientPrice = 0;

    @ApiModelProperty(value = "訂單食材數量")
    private Double quantityRequired = 0D;

}
