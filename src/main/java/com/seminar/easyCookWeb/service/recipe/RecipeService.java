package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.app.RecipeAppModel;
import com.seminar.easyCookWeb.model.recipe.update.RecipeUpdateModel;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.service.ingredient.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.List;
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
    private IngredientRepository ingredientRepository;
    @Autowired
    private RecipeIngredientService recipeIngredientService;
    @Autowired
    private RecipeMapper mapper;
    @Autowired
    private IngredientMapper ingredientMapper;
    @Autowired
    private RecipeImageService recipeImageService;


    /**
     * 新增食譜
     *
     * @param recipeModel - 食譜物件
     * @return 新增完成後的食譜 DTO 物件
     */
    public Optional<RecipeModel> createRecipe(RecipeModel recipeModel) {
        return Optional.of(mapper.toPOJO(recipeModel))
                .filter(pojo -> recipeRepository.ExistNameAndVersionToCreate(pojo.getName(), pojo.getVersion()).isEmpty())
                .map(recipeRepository::save)
                .map(mapper::toModel)
                .map(it -> {
                    return Optional.of(it.toBuilder()
                            .recipeSteps(
                                    recipeModel.getRecipeSteps().stream()
                                            .map(recipeStepModel -> recipeStepService.create(it.getId(), recipeStepModel))
                                            .map(Optional::get)
                                            .collect(Collectors.toList())
                            )
                            .recipeIngredients(
                                    recipeModel.getRecipeIngredients().stream()
                                            .map(recipeIngredientModel -> recipeIngredientService.create(it.getId(), recipeIngredientModel))
                                            .map(Optional::get)
                                            .collect(Collectors.toList())
                            )
                            .build());
                })
                .orElseThrow(() -> new EntityCreatedConflictException("There is a recipe have same name and version already existed!"));
    }

    /**
     * 透過食譜Id尋找食譜
     *
     * @param id - 食譜id
     * @return 找到的食譜DTO物件
     */
    public Optional<RecipeModel> findById(Long id) {
        return recipeRepository.findById(id)
                .map(mapper::toModel);
    }

    /**
     * 透過食譜名稱尋找食譜
     * @param name
     * @return
     */
    public Optional<Iterable<RecipeModel>> findByName(String name) {
        return recipeRepository.findByPartName(name)
                .map(mapper::toIterableModel);
    }

    /**
     * 透過版本來尋找所有的食譜
     * @param version
     * @return 食譜model
     */
    public Optional<Iterable<RecipeModel>> findByVersion(String version) {
        return recipeRepository.findAllByVersion(version)
                .map(mapper::toIterableModel);
    }

    /**
     * 取得所有的食譜
     * @return 食譜model
     */
    public Optional<Iterable<RecipeModel>> findAll() {
        return Optional.of(recipeRepository.findAll())
                .map(mapper::toIterableModel);
    }

    /**
     * 透過食譜id刪除食譜
     * @param id
     * @return 已刪除的食譜model
     */
    public Optional<RecipeModel> delete(Long id) {
        return recipeRepository.findById(id)
                .map(it -> {
                    try {
                        recipeRepository.deleteById(it.getId());
                        return it;
                    } catch (Exception ex) {
                        throw new BusinessException("Cannot Deleted " + it.getId() + " recipe");
                    }
                })
                .map(mapper::toModel);
    }

    public Optional<List<RecipeAppModel>> recipeOverVeiw(){
        List<RecipeAppModel> recipeAppModels = new LinkedList<>();

        recipeRepository.findAll()
                .forEach(recipe -> {
                    String image = "No Image";
                    if(recipe.getPhotos().stream().findFirst().isPresent())
                        image =  recipe.getPhotos().stream().findFirst().get().getName();

                    recipeAppModels.add(
                            RecipeAppModel.builder()
                                    .id(recipe.getId())
                                    .version(recipe.getVersion())
                                    .likesCount(recipe.getLikesCount())
                                    .name(recipe.getName())
                                    .price(recipe.getPrice())
                                    .photo(recipeImageService
                                            .getS3PhotoUrl(image)
                                    )
                            .build()
                    );
                });
        return Optional.of(recipeAppModels);
    }

    /**
     * 更新食譜
     * @param iid
     * @param request
     * @return 更新結果食譜model
     */
    public Optional<RecipeModel> update(Long iid, RecipeUpdateModel request) {
        Recipe origin = recipeRepository.findById(iid).orElseThrow(() -> new EntityNotFoundException("Cannot find recipe"));
        log.info("[recipeService] -> [update] -> origin : " + origin);
        if (recipeRepository
                .ExistNameAndVersionToUpdate(
                        request.getName(),
                        request.getVersion(),
                        iid).isPresent()
        ) {
            log.info("[recipeService] -> [update] -> EntityCreatedConflictException ");
            throw new EntityCreatedConflictException("There is a recipe have same name and version already existed!");
        }

        return Optional.of(origin)
                .map(it -> {
                    mapper.update(request, origin);
                    log.info("[recipeService] -> [update] -> 178: mapper.update origin :" + origin);

                    return origin;
                })
                .map(recipeRepository::save)
                .map(recipedb -> {

                    log.info("[recipeService] -> [update] -> 185: after recipeRepository::save recipeDB" + recipedb);
                    List<RecipeStep> recipeSteps = request.getRecipeSteps().stream()
                            .map(recipeStep -> recipeStepService.updateStep(recipedb.getId(), recipeStep))
                            .map(Optional::get)
                            .collect(Collectors.toList());

                    log.info("[recipeService] -> [update] -> 192: new recipeSteps" + recipeSteps);
                    recipedb.setRecipeSteps(recipeSteps);
                    log.info("[recipeService] -> [update] -> 195: after set recipeStep -> recipedb" + recipedb);

                    List<RecipeIngredient> recipeIngredients = request.getRecipeIngredients().stream()
                            .map(ingredientModel -> {
                                return recipeIngredientService.updateIngredient(recipedb.getId(), ingredientModel);
                            })
                            .map(Optional::get)
                            .collect(Collectors.toList());
                    log.info("[recipeService] -> [update] -> 192: new recipeIngredients" + recipeIngredients);

                    recipedb.setRecipeIngredients(recipeIngredients);
                    log.info("[recipeService] -> [update] -> 200: after set steps and ingredients : " + recipedb);

                    return recipedb;
                })
                .map(mapper::toModel);
    }


}
