package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeIngredientMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeIngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeIngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipeIngredientService {
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RecipeIngredientMapper mapper;

    public Optional<RecipeIngredientModel> createIngredient(Long recipeId, RecipeIngredientModel ingredientModel){
        return Optional.of(mapper.toPOJO(ingredientModel))
                .map(it -> it.toBuilder()
                        .recipe(
                                recipeRepository.findById(recipeId)
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find recipe"))
                        ).ingredient(
                                ingredientRepository.findById(ingredientModel.getIngredient().getId())
                                    .orElseThrow(()-> new EntityNotFoundException("Cannot find IngredinetId =>"+ingredientModel.getIngredient().getId() +" for recipe"))
                        )
                        .Quantityrequired(
                                ingredientModel.getQuantityrequired()
                        )
                        .build()
                ).map(recipeIngredientRepository::save)
                .map(mapper::toModel);
    }

}
