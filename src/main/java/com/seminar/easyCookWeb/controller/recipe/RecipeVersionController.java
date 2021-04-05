package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.search.RecipeVersionModel;
import com.seminar.easyCookWeb.service.recipe.RecipeVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/recipe/version" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "食譜版本查詢連接口", description = "提供食譜版本相關的查詢 Rest API")
public class RecipeVersionController {
    @Autowired
    private RecipeVersionService recipeVersionService;


    @GetMapping("/{recipeId}")
    @ApiOperation("透過食譜ID尋找相關的食譜版本: Search Recipe By Id {EVERYONE CAN ACCESS}")
    public ResponseEntity<RecipeVersionModel> getById(@PathVariable Long recipeId){
        return recipeVersionService.getRecipeRelativeVersionById(recipeId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cann not find any recipes"));
    }
}
