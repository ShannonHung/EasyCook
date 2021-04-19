package com.seminar.easyCookWeb.service.cart;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.cart.CartCustomMapper;
import com.seminar.easyCookWeb.mapper.cart.CartMapper;
import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeCustomizeRequest;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeCustomModel;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.repository.cart.CartRecipeCustomizeRepository;
import com.seminar.easyCookWeb.repository.cart.CartRecipeRepository;
import com.seminar.easyCookWeb.service.ingredient.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartRecipeCustomService {
    @Autowired
    private CartCustomMapper mapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartRecipeRepository cartRecipeRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private IngredientMapper ingredientMapper;
    @Autowired
    private CartRecipeCustomizeRepository customizeRepository;

    public Optional<CartRecipeCustomModel> create(Long cartId, CartRecipeCustomizeRequest request, Authentication authentication) {

        return Optional.of(request)
                .map((req) -> CartRecipeCustomize.builder()
                        .ingredient(ingredientService.readByIngredientId(req.getIngredientId())
                                .map(ingredientMapper::toPOJO)
                                .orElseThrow(()-> new EntityNotFoundException("Cannot find "+ req.getIngredientId()+" this ingredient")))
                        .quantityRequired(req.getQuantityRequired()).build()
                )
                .map(it -> it.toBuilder().cartRecipe(isOwner(cartId, authentication)).build())
                .map(customizeRepository::save)
                .map(mapper::toModel);
    }

    public Optional<CartRecipeModel> delete(Long cartId, Long customId, Authentication authentication) {
        isOwner(cartId, authentication);
        customizeRepository.deleteById(customId);
        return Optional.of(cartRecipeRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find shopping cart item")))
                .map(cartMapper::toModel);
    }

    public Optional<CartRecipeCustomize> updateCustom(Long cartId, CartRecipeCustomModel customModel) {
        return Optional.of(mapper.toPojo(customModel))
                .map(it -> {
                            CartRecipe cart = cartRecipeRepository.findById(cartId)
                                    .orElseThrow(() -> new EntityNotFoundException("Cannot find recipe"));
                            it.setCartRecipe(cart);
                            return it;
                        }
                ).map(customizeRepository::save);
    }

    public CartRecipe isOwner(Long cartId, Authentication authentication){
        CartRecipe cart = cartRecipeRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find shopping cart item"));
        if(!cart.getMember().getAccount().equals(authentication.getName())) throw new BusinessException("You are not the member who can edit this cart item");
        else {
            return cart;
        }
    }

}
