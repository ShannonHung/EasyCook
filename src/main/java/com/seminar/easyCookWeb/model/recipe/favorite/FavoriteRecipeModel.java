package com.seminar.easyCookWeb.model.recipe.favorite;

import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.user.MemberResponse;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
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
@ApiModel("我的最愛食譜樣式")
public class FavoriteRecipeModel {
    @ApiModelProperty("該清單的使用者")
    private MemberResponse member;

    @ApiModelProperty("該使用者的最愛食譜清單")
    private List<RecipeModel> recipes = new LinkedList<>();

}
