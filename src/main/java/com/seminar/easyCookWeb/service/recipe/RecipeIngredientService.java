package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.mapper.recipe.RecipeIngredientMapper;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeIngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.model.recipe.update.RecipeIngredientUpdateModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeIngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.service.ingredient.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RecipeIngredientService {
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private RecipeIngredientMapper mapper;
    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private IngredientMapper ingredientMapper;

    /**
     * 針對整個食譜傳入json時一個個針對json裡面的食譜食材做新增
     *
     * @param recipeId
     * @param ingredientModel
     * @return 回傳在資料庫建立的食譜model
     */
    public Optional<RecipeIngredientModel> create(Long recipeId, RecipeIngredientModel ingredientModel){
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

    /**
     * 只傳入食譜食材的相關資料針對食材數量做更新
     *
     * @param recipeId
     * @param ingredientModel
     * @return 回傳最新食譜model
     */
    public Optional<RecipeIngredientModel> createIngredient(Long recipeId, RecipeIngredientModel ingredientModel) {
        return Optional.of(mapper.toPOJO(ingredientModel))
                .map(it -> it.toBuilder()
                        .recipe(
                                recipeRepository.findById(recipeId)
                                        .orElseThrow(() -> new EntityNotFoundException("Cannot find recipe"))
                        ).ingredient(
                                ingredientRepository.findById(ingredientModel.getIngredient().getId())
                                        .orElseThrow(() -> new EntityNotFoundException("Cannot find IngredinetId =>" + ingredientModel.getIngredient().getId() + " for recipe"))
                        )
                        .quantityRequired(
                                ingredientModel.getQuantityRequired()
                        )
                        .build()
                ).map(recipeIngredientRepository::save)
                .map(mapper::toModel);
    }

    /**
     * 透過食譜食材的id 做刪除
     *
     * @param recipeId
     * @param recipeStepId
     * @return 回傳刪除該食譜食材後的食譜
     */
    public Optional<RecipeModel> delete(Long recipeId, Long recipeStepId) {
        try{
            recipeIngredientRepository.deleteById(recipeStepId);
        }catch (Exception e) {
            throw new EntityNotFoundException("Cannot find this Recipe Ingredient");
        }
        return Optional.of(recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find recipe")))
                .map(recipeMapper::toModel);
    }

    /**
     * 在更新整個json的時候，針對有提供德食譜食材做更新
     *
     * @param recipeId
     * @param ingredientModel
     * @return 回傳最新的食譜食材model
     */
    public Optional<RecipeIngredient> updateIngredient(Long recipeId, RecipeIngredientUpdateModel ingredientModel) {
        return Optional.of(mapper.toPOJO(ingredientModel))
                .map(recipeIngredient -> {
                            log.info("[recipeIngredientService] -> [updateIngredient] -> recipeIngredient  -> " + recipeIngredient);
                            RecipeIngredient recipeIngredientdb = recipeIngredientRepository.findById(ingredientModel.getId()).get();
                            mapper.update(recipeIngredient, recipeIngredientdb);
                            log.info("[recipeIngredientService] -> [updateIngredient] -> recipeIngredientdb  -> " + recipeIngredientdb);
                            return recipeIngredientdb;

                        }
                )
                .map(recipeIngredientRepository::save);
    }

}
