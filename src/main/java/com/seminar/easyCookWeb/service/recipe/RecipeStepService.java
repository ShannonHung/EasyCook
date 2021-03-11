package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeStepMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipeStepService {
    @Autowired
    private RecipeStepRepository recipeStepRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeStepMapper mapper;

    public Optional<RecipeStepModel> createStep(Long recipeId,RecipeStepModel stepModel){
        return Optional.of(mapper.toPOJO(stepModel))
                .map(it -> it.toBuilder()
                        .recipe(
                           recipeRepository.findById(recipeId)
                                .orElseThrow(()-> new EntityNotFoundException("Cannot find recipe"))
                        ).build()
                ).map(recipeStepRepository::save)
                .map(mapper::toModel);
    }

    public Optional<RecipeStep> updateStep(Long recipeId, RecipeStep recipeStep){
        return Optional.of(recipeStep)
                .map(it -> it.toBuilder()
                        .recipe(
                                recipeRepository.findById(recipeId)
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find recipe"))
                        ).build()
                ).map(recipeStepRepository::save);
    }

}
