package com.seminar.easyCookWeb.model.cart.response;

import com.seminar.easyCookWeb.model.cart.request.CartRecipeCustomizeRequest;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.user.MemberResponse;
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
@ApiModel("購物車 回應Entity樣式")
public class CartRecipeModel {
    private Long id;

    @ApiModelProperty(value = "是否為客製化購物車項目")
    @Builder.Default
    private Boolean IsCustomize = false;

    @ApiModelProperty(value = "是否為客製化購物車項目")
    @Builder.Default
    private Double sum = 0D;

    @ApiModelProperty(value = "會員資料")
    private MemberResponse member;

    @ApiModelProperty(value = "食譜")
    private RecipeModel recipe;

    @ApiModelProperty(value = "食譜客製化食材")
    @Builder.Default
    private List<CartRecipeCustomModel> customize = new LinkedList<>();

    @ApiModelProperty(value = "該食譜圖片")
    private String RecipeImage;


}
