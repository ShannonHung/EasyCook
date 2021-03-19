package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.mapper.recipe.RecipeStepMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
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

    @Autowired
    private RecipeMapper recipeMapper;

    /**
     * 建立整個食譜json時針對食譜步驟進行更新
     * @param recipeId
     * @param stepModel
     * @return 已儲存的食譜步驟 model
     */
    public Optional<RecipeStepModel> create(Long recipeId,RecipeStepModel stepModel){
        return Optional.of(mapper.toPOJO(stepModel))
                .map(it -> it.toBuilder()
                        .recipe(
                                recipeRepository.findById(recipeId)
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find recipe"))
                        ).build()
                ).map(recipeStepRepository::save)
                .map(mapper::toModel);
    }

    /**
     * 單獨針對食譜建立食譜步驟
     * @param recipeId
     * @param stepModel
     * @return 新增完食譜步驟的食譜
     */
    public Optional<RecipeStepModel> createStep(Long recipeId, RecipeStepModel stepModel){
        return Optional.of(mapper.toPOJO(stepModel))
                .map(it -> it.toBuilder()
                        .recipe(
                                recipeRepository.findById(recipeId)
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find recipe"))
                        ).build()
                ).map(recipeStepRepository::save)
                .map(mapper::toModel);
    }


    /**
     * 單獨針對食譜步驟進行刪除
     * @param recipeId
     * @param recipeStepId
     * @return 回傳刪除玩食譜步驟的食譜
     */
    public Optional<RecipeModel> delete(Long recipeId, Long recipeStepId){
        recipeStepRepository.deleteById(recipeStepId);
        return Optional.of(recipeRepository.findById(recipeId)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find recipe")))
                .map(recipeMapper::toModel);
    }

    /**
     * 單獨針對食譜步驟進行更新
     * @param recipeId
     * @param recipeStepModel
     * @return 回傳更新完成並且含有recipe_step_id的食譜步驟
     */
    public Optional<RecipeStep> updateStep(Long recipeId, RecipeStepModel recipeStepModel){
        return Optional.of(mapper.toPOJO(recipeStepModel))
                .map(it -> {
                        Recipe recipe = recipeRepository.findById(recipeId)
                                .orElseThrow(() -> new EntityNotFoundException("Cannot find recipe"));
                        it.setRecipe(recipe);
                            return it;
                        }
                ).map(recipeStepRepository::save);
    }

}
