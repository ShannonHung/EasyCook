package com.seminar.easyCookWeb.RecipeTest;

import com.seminar.easyCookWeb.EasyCookWebApplication;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Transactional
@Slf4j
@SpringBootTest(classes = EasyCookWebApplication.class)
public class RecipeTest {
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    IngredientRepository ingredientRepository;

    @Test
    public void testRecipe(){
        Recipe recipe = Recipe.builder()
                .name("三明治")
                .link("..//")
                .likesCount(10).build();

        RecipeStep step1 = RecipeStep.builder()
                .recipe(recipe)
                .note("切水果")
                .startTime(100).build();

        RecipeIngredient ingredient = RecipeIngredient.builder()
                .ingredient(Ingredient.builder().name("ingredient").build())
                .quantityRequired(10D)
                .build();

        List<RecipeStep> steps = new LinkedList<>();
        List<RecipeIngredient> ingredients = new LinkedList<>();
        ingredients.add(ingredient);
        steps.add(step1);
    }
}
