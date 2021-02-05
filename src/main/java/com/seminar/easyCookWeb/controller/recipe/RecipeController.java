package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.service.recipe.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/recipe" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "食譜Recipe連接口", description = "提供食譜相關的 Rest API")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping("/create")
    @ApiOperation("建立食譜: Create Recipe {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<RecipeModel> create(@RequestBody RecipeModel recipeModel){
        log.info("[recipe create] => " + recipeModel);
        return recipeService.createRecipe(recipeModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create Recipe!"));
    }

    @GetMapping("/{recipeId}")
    @ApiOperation("透過食譜ID尋找食譜: Search Recipe By Id {EVERYONE CAN ACCESS}")
    public ResponseEntity<RecipeModel> getById(@PathVariable Long recipeId){
        return recipeService.findById(recipeId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cann not find any recipes"));
    }

    @PostMapping("/name")
    @ApiOperation("透過食譜名稱尋找食譜: Search Recipe By Name {EVERYONE CAN ACCESS}")
    public ResponseEntity<Iterable<RecipeModel>> getByName(@RequestBody RecipeModel recipeModel){
        return recipeService.findByName(recipeModel.getName())
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cann not find any recipes"));
    }

    @GetMapping("/all")
    @ApiOperation("取得所有食譜: Get all recipes {EVERYONE CAN ACCESS}")
    public ResponseEntity<Iterable<RecipeModel>> findAll(){
        return recipeService.findAll()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cann not find any recipes"));
    }
}
