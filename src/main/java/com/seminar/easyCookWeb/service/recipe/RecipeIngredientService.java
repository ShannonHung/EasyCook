package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeIngredientMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeIngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
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
                        .quantityRequired(
                                ingredientModel.getQuantityRequired()
                        )
                        .build()
                ).map(recipeIngredientRepository::save)
                .map(mapper::toModel);
    }

//    public Optional<RecipeIngredient> updateIngredient(Long recipeId, RecipeIngredientModel ingredientModel){
//        return Optional.of(mapper.toPOJO(ingredientModel))
//                .map(recipeIngredient -> {
//                    //如果iid是空的代表有新增內容
//                    Optional<RecipeIngredient> recipeIngredientdb = recipeIngredientRepository.findById(ingredientModel.getId());
//                    Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(()-> new EntityNotFoundException("Cannot find recipe"));
//                    Ingredient ingredient = ingredientRepository.findById(ingredientModel.getIngredient().getId()).orElseThrow(()-> new EntityNotFoundException("Cannot find IngredinetId =>"+ ingredientModel.getIngredient().getId() +" for recipe"));
//
//                    if(recipeIngredientdb.isEmpty()){
//                        //如果iid是空的代表是新資料，因此我要重新save一個新的
//                        return recipeIngredient.toBuilder()
//                                .recipe(recipe)
//                                .ingredient(ingredient)
//                                .quantityRequired(ingredientModel.getQuantityRequired())
//                                .build();
//                    }else{
//                        //如果iid不是空的表示有該筆資料要進行更新，所以要針對資料庫挖出來的進行修改，並且儲存
//                        recipeIngredientdb.get().toBuilder()
//                                .recipe(recipe)
//                                .quantityRequired(ingredientModel.getQuantityRequired());
//                        return recipeIngredient;
//                    }
//                }
//                ).map(recipeIngredientRepository::save);
//    }

}
