package com.seminar.easyCookWeb.controller.cart;

import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.service.cart.CartRecipeService;
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
import java.util.List;

@RestController
@RequestMapping(value = "/cart" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "購物車Shopping Cart連接口", description = "提供購物車相關的 Rest API")
public class CartController {
    @Autowired
    CartRecipeService cartRecipeService;

    @PostMapping("/customize/add")
    @ApiOperation("加入購物車: Add Recipe into Shopping Cart ('ROLE_MEMBER', 'ROLE_VIP')")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<CartRecipeModel> addRecipeToCartCustomize(@Valid @RequestBody CartRecipeRequest request, Authentication authentication){
        return cartRecipeService.addByCustomize(request, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot add this recipe Into Cart!"));
    }

    @GetMapping("/default/add/{recipeId}")
    @ApiOperation("加入購物車: Add Recipe into Shopping Cart ('ROLE_MEMBER', 'ROLE_VIP')")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<CartRecipeModel> addRecipeToCartDefault(@PathVariable("recipeId") Long recipeId, Authentication authentication){
        return cartRecipeService.addByDefault(recipeId, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot add this recipe Into Cart!"));
    }

    @GetMapping("/all")
    @ApiOperation("瀏覽所有購物車項目: Review All Shopping Cart List ('ROLE_MEMBER')")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<Iterable<CartRecipeModel>> getAllCart(Authentication authentication){

        return cartRecipeService.findAllByMember(authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot get all shopping cart list!"));
    }

    @GetMapping("/get/{cartId}")
    @ApiOperation("取得特定購物車項目: Get Shopping Cart By ID ('ROLE_MEMBER', 'ROLE_VIP')")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<CartRecipeModel> getAllCart(@PathVariable("cartId") Long cartId){
        return cartRecipeService.getCartById(cartId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("CANNOT FIND THE SHOPPING CART! ID " + cartId));
    }

    @DeleteMapping("/delete/{cartId}")
    @ApiOperation("刪除購物車項目by id: Delete Shopping Cart Item By Id ('ROLE_MEMBER')")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<CartRecipeModel> deleteCartById(@PathVariable("cartId") Long cartId, Authentication authentication){
        return cartRecipeService.deleteById(cartId, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot delete this shopping cart item!"));
    }

    @PatchMapping("/update/{cartId}")
    @ApiOperation("更新購物車項目by id: Update Shopping Cart Item By Id ('ROLE_MEMBER')")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<CartRecipeModel> updateCartById(@PathVariable("cartId") Long cartId, @Valid @RequestBody CartRecipeModel request, Authentication authentication){
        return cartRecipeService.update(cartId, request, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot delete this shopping cart item!"));
    }
}
