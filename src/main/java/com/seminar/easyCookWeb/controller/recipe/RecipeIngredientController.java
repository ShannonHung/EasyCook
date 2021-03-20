package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.recipe.RecipeIngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.repository.recipe.RecipeIngredientRepository;
import com.seminar.easyCookWeb.service.recipe.RecipeIngredientService;
import com.seminar.easyCookWeb.service.recipe.RecipeStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/recipe" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "食譜使用食材Recipe Ingredient連接口", description = "提供食譜使用食材相關的 Rest API")
public class RecipeIngredientController {
    @Autowired
    private RecipeIngredientService recipeIngredientService;

    @PostMapping("/{recipeId}/ingredient/create")
    @ApiOperation("新增食譜的食材: Create Recipe Ingredient {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<RecipeIngredientModel> create(@PathVariable Long recipeId, @Valid @RequestBody RecipeIngredientModel request){
        return recipeIngredientService.createIngredient(recipeId, request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create Recipe! Maybe have Duplicated Recipe Name"));
    }

    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("刪除食譜的食材: Delete Recipe Ingredient {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @DeleteMapping(path = "/{recipeId}/ingredient/delete/{recipeIngredientId}")
    public ResponseEntity<RecipeModel> deleteById(@PathVariable Long recipeId, @PathVariable Long recipeIngredientId) {
        return recipeIngredientService.delete(recipeId, recipeIngredientId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete Ingredient fail"));
    }
}
