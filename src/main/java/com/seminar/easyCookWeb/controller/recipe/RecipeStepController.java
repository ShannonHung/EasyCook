package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.repository.recipe.RecipeStepRepository;
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
@Api(tags = "食譜使用步驟Recipe Step連接口", description = "提供食譜步驟相關的 Rest API")
public class RecipeStepController {
    @Autowired
    private RecipeStepService recipeStepService;

    @PostMapping("/{recipeId}/step/create")
    @ApiOperation("新增食譜步驟: Create Recipe Step {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<RecipeStepModel> create(@PathVariable Long recipeId, @Valid @RequestBody RecipeStepModel request){
        return recipeStepService.createStep(recipeId, request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create Recipe! Maybe have Duplicated Recipe Name"));
    }

    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("刪除食譜步驟: Create Recipe Step  {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @DeleteMapping(path = "/{recipeId}/step/delete/{recipeStepId}")
    public ResponseEntity<RecipeModel> deleteById(@PathVariable Long recipeId, @PathVariable Long recipeStepId) {
        return recipeStepService.delete(recipeId, recipeStepId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete Ingredient fail"));
    }

}
