package com.seminar.easyCookWeb.model.cart.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("購物車 請求Entity樣式")
public class CartRecipeRequest {
    private long id;

    @NotNull
    @ApiModelProperty(value = "食譜id", example = "6")
    private Long recipeId;

    @ApiModelProperty(value = "食譜客製化食材")
    @Builder.Default
    private List<CartRecipeCustomizeRequest> customize = new LinkedList<>();
}
