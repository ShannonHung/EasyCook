package com.seminar.easyCookWeb.model.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import com.seminar.easyCookWeb.pojo.order.OrderItemCustom;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("訂單細項 model 請求Entity樣式")
public class OrderItemModel {
    private long id;

    @ApiModelProperty(value = "訂單產品")
    private RecipeModel recipe;

    @ApiModelProperty(value = "產品內的食材")
    private List<OrderItemCustomModel> customize = new LinkedList<>();

}
