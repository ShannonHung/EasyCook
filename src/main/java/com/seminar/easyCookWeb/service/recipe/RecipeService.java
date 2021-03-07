package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.service.ingredient.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeStepService recipeStepService;
    @Autowired
    private RecipeIngredientService recipeIngredientService;
    @Autowired
    private RecipeMapper mapper;

    /**
     * 新增食譜 以及 食譜步驟
     * @param recipeModel - 食譜物件
     * @return 新增完成後的食譜 DTO 物件
     */
    public Optional<RecipeModel> createRecipe(RecipeModel recipeModel){
        return Optional.of(mapper.toPOJO(recipeModel))
                .filter(pojo-> recipeRepository.findByName(pojo.getName()).isEmpty())
                .map(recipeRepository::save)
                .map(mapper::toModel)
                .map(it -> {
                    return Optional.of(it.toBuilder()
                            .recipeSteps(
                                    recipeModel.getRecipeSteps().stream()
                                            .map(recipeStepModel -> recipeStepService.createStep(it.getId(), recipeStepModel))
                                            .map(Optional::get)
                                            .collect(Collectors.toList())
                            )
                            .recipeIngredients(
                                    recipeModel.getRecipeIngredients().stream()
                                            .map(recipeIngredientModel -> recipeIngredientService.createIngredient(it.getId(),recipeIngredientModel))
                                            .map(Optional::get)
                                            .collect(Collectors.toList())
                            )
                            .build());
                })
                .orElseThrow(() -> new EntityCreatedConflictException("An Recipe with same name have already existed!"));
    }

    /**
     * 透過食譜Id尋找食譜
     * @param id - 食譜id
     * @return 找到的食譜DTO物件
     */
    public Optional<RecipeModel> findById(Long id){
        return recipeRepository.findById(id)
                .map(mapper::toModel);
    }

    public Optional<Iterable<RecipeModel>> findByName(String name){
        return recipeRepository.findByPartName(name)
                .map(mapper::toIterableModel);
    }

    public Optional<Iterable<RecipeModel>> findAll(){
        return Optional.of(recipeRepository.findAll())
                .map(mapper::toIterableModel);
    }

    public Optional<RecipeModel> delete(Long id){
        return recipeRepository.findById(id)
                .map(it ->{
                    try {
                        recipeRepository.deleteById(it.getId());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted " +it.getId()+ " recipe");
                    }
                })
                .map(mapper::toModel);
    }

    public Optional<RecipeModel> update(Long iid, RecipeModel request) {
        return Optional.of(recipeRepository.findById(iid))
                .map(it -> {
                    Recipe origin = it.orElseThrow(() -> new EntityNotFoundException("Cannot find Ingredient"));
                    if(recipeRepository.ExistName(request.getName(), iid) > 0){
                        throw new EntityCreatedConflictException("this ingredient have already in used!");
                    }else{
                        mapper.update(request, origin);
                        return origin;
                    }
                })
                .map(recipeRepository::save)
                .map(mapper::toModel);
    }
}
