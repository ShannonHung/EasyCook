package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.recipe.search.RecipeVersionModel;
import com.seminar.easyCookWeb.model.recipe.search.RecipeVersionRelativeModel;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecipeVersionService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeMapper recipeMapper;

    public Optional<RecipeVersionModel> getRecipeRelativeVersionById(Long recipeId){
        Recipe origin = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Cann not find any recipes"));
        return Optional.of(origin)
                .map(recipe -> recipeRepository.findByName(recipe.getName()))
                .map((recipes -> {
                    List<RecipeVersionRelativeModel> recipesModel = new LinkedList<>();

                    for (Recipe recipe: recipes.get()){
                        recipesModel.add(RecipeVersionRelativeModel.builder()
                                .recipeId(recipe.getId())
                                .version(recipe.getVersion()).build());
                    }

                    return RecipeVersionModel.builder()
                            .currentRecipe(recipeMapper.toModel(origin))
                            .existedVersions(recipesModel).build();
                }));
    }
}
