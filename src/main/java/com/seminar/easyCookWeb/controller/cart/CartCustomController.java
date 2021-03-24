package com.seminar.easyCookWeb.controller.cart;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeCustomizeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeCustomModel;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.service.cart.CartRecipeCustomService;
import com.seminar.easyCookWeb.service.recipe.RecipeStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/cart" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "購物車客製化食材Shopping Cart Ingredient連接口", description = "提供購物車客製化相關的 Rest API")
public class CartCustomController {
    @Autowired
    private CartRecipeCustomService customService;

    @PostMapping("/{cartId}/custom/create")
    @ApiOperation("新增購物車客製化食材: Create Shopping Cart Custom Ingredient {ROLE_MEMBER}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public ResponseEntity<CartRecipeCustomModel> create(@PathVariable Long cartId, @Valid @RequestBody CartRecipeCustomizeRequest request  ,Authentication authentication){
        return customService.create(cartId, request, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create Recipe! Maybe have Duplicated Recipe Name"));
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @ApiOperation("刪除購物車客製化食材: Remove Shopping Cart Custom Ingredient {ROLE_MEMBER}")
    @DeleteMapping(path = "/{cartId}/custom/delete/{customId}")
    public ResponseEntity<CartRecipeModel> deleteById(@PathVariable Long cartId, @PathVariable Long customId, Authentication authentication) {
        return customService.delete(cartId, customId, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete Ingredient fail"));
    }
}
