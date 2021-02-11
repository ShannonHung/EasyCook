package com.seminar.easyCookWeb.config;

import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultDataConfig implements ApplicationRunner {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Recipe recipe = Recipe.builder()
                .name("三明治")
                .link("..//")
                .likesCount(10).build();

        RecipeStep step1 = RecipeStep.builder()
                .recipe(recipe)
                .note("切水果")
                .startTime("0:51").build();

        RecipeIngredient ingredient = RecipeIngredient.builder()
                .ingredient(Ingredient.builder().name("ingredient").build())
                .Quantityrequired(10D)
                .recipe(recipe)
                .build();

        List<RecipeStep> steps = new LinkedList<>();
        List<RecipeIngredient> ingredients = new LinkedList<>();
        ingredients.add(ingredient);
        steps.add(step1);

        recipe.setRecipeSteps(steps);
        recipe.setRecipeIngredients(ingredients);

        Recipe dbre = recipeRepository.save(recipe);
    }
}