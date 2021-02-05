package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
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
    private RecipeMapper mapper;

    /**
     * 新增食譜 以及 食譜步驟
     * @param recipeModel - 食譜物件
     * @return 新增完成後的食譜 DTO 物件
     */
    public Optional<RecipeModel> createRecipe(RecipeModel recipeModel){
        return Optional.of(mapper.toPOJO(recipeModel))
                .map(recipeRepository::save)
                .map(mapper::toModel)
                .map(it -> it.toBuilder()
                        .recipeSteps(
                                recipeModel.getRecipeSteps().stream()
                                .map(recipeStepModel -> recipeStepService.createStep(it.getId(), recipeStepModel))
                                .map(Optional::get)
                                .collect(Collectors.toList())
                        ).build());
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
        return recipeRepository.findByName(name)
                .map(mapper::toIterableModel);
    }

    public Optional<Iterable<RecipeModel>> findAll(){
        return Optional.of(recipeRepository.findAll())
                .map(mapper::toIterableModel);
    }
}
