package com.seminar.easyCookWeb.config;

import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
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

        List<RecipeStep> steps = new LinkedList<>();
        steps.add(step1);

        recipe.setRecipeSteps(steps);

        Recipe dbre = recipeRepository.save(recipe);
    }
}