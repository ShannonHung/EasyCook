package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.service.recipe.FavoriteRecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/myFavorite" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "使用者我的最愛連接口", description = "提供我的最愛相關的 Rest API")
public class FavoriteRecipeController {
    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    @GetMapping("/add/{recipeId}")
    @ApiOperation("將食譜ID加入我的最愛: Add the recipe to My Favorite ('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<List<RecipeModel>> getById(@PathVariable Long recipeId, Authentication authentication){
        return favoriteRecipeService.create(recipeId, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cann not find any recipes"));
    }

    @GetMapping("/all")
    @ApiOperation("取得所有我的最愛: Get all myFavorite {('ROLE_ADMIN', 'ROLE_EMPLOYEE')}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<List<RecipeModel>> findAll(Authentication authentication){
        return favoriteRecipeService.getAllFavorite(authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cann not find any recipes"));
    }

    @DeleteMapping(path = "/remove/{recipeId}")
    @ApiOperation("從我的最愛中移除: Remove the recipe from myFavorite ('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity deleteById(@PathVariable Long recipeId, Authentication authentication) {
//        favoriteRecipeService.removeRecipe(recipeId, authentication);
//        return ResponseEntity.ok().build();
        return favoriteRecipeService.removeRecipe(recipeId, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete Ingredient fail"));
    }



}
