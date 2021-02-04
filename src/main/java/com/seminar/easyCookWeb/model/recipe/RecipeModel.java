package com.seminar.easyCookWeb.model.recipe;

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
public class RecipeModel {
    private long id;

    private String name;

    private byte[] photo;

    private String link;

    private int likesCount;

    @Builder.Default
    private List<RecipeStepModel> recipeSteps = new LinkedList<>();

    @Builder.Default
    private List<RecipeIngredientModel> recipeIngredients = new LinkedList<>();

}
