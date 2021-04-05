package com.seminar.easyCookWeb.service.cart;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.cart.CartMapper;
import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeCustomModel;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.repository.cart.CartRecipeCustomizeRepository;
import com.seminar.easyCookWeb.repository.cart.CartRecipeRepository;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import com.seminar.easyCookWeb.service.ingredient.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartRecipeService {
    @Autowired
    private CartRecipeRepository cartRecipeRepository;
    @Autowired
    private CartRecipeCustomizeRepository customizeRepository;
    @Autowired
    private CartRecipeCustomService customService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private IngredientMapper ingredientMapper;
    @Autowired
    private CartMapper cartMapper;

    public Optional<CartRecipeModel> add(CartRecipeRequest request, Authentication authentication){
        return Optional.of(request)
                .map((re) -> {
                    Member member = memberRepository.findByAccount(authentication.getName()).orElseThrow(()-> new EntityNotFoundException("Cannot find the user, maybe you are not member."));
                    return cartRecipeRepository.save(CartRecipe.builder()
                            .member(member)
                            .IsCustomize(re.getIsCustomize())
                            .recipe(recipeRepository.findById(request.getRecipeId())
                                    .orElseThrow(()-> new EntityNotFoundException("Cannot find this recipe")))
                            .build());
                })
                .map((cart) -> {
                    List<CartRecipeCustomize> customize = new LinkedList<>();

                    request.getCustomize().forEach((cus) -> {
                        CartRecipeCustomize cust = CartRecipeCustomize.builder()
                                .quantityRequired(cus.getQuantityRequired())
                                .cartRecipe(cart)
                                .build();
                        cust.setIngredient(ingredientService.readByIngredientId(cus.getIngredientId())
                                .map(ingredientMapper::toPOJO)
                                .orElseThrow(()-> new EntityNotFoundException("Cannot find "+ cus.getIngredientId()+" this ingredient")));
                        cust = customizeRepository.save(cust);
                        customize.add(cust);
                    });

                    cart.setCustomize(customize);
                    return cartRecipeRepository.save(cart);
                })
                .map((cart)-> {
                    CartRecipeModel model = cartMapper.toModel(cart);
                    return model;
                });
    }
    public Optional<Iterable<CartRecipeModel>> findAllByMember(Authentication authentication){
        Member member = memberRepository.findByAccount(authentication.getName()).orElseThrow(()-> new EntityNotFoundException("Cannot find user, or you are not the member"));
        return cartRecipeRepository.findAllByMemberId(member.getId())
                .map(cartMapper::toIterableModel);

    }

    public Optional<CartRecipeModel> deleteById(Long id, Authentication authentication){

        CartRecipe model = cartRecipeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cannot find this cart item"));
        if(!authentication.getName().equals(model.getMember().getAccount())){
            throw new BusinessException("These cart's owner is not you, so you cannot delete this cart item");
        }
        cartRecipeRepository.deleteById(id);
        return Optional.of(model)
                .map(cartMapper::toModel);
    }

    public Optional<CartRecipeModel> update(Long cartId, CartRecipeModel request, Authentication authentication){
        CartRecipe origin = cartRecipeRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("cannot find this cart item"));
        return Optional.of(origin)
                .map(it -> {
                    cartMapper.update(request, origin);
                    return origin;
                })
                .map(cartRecipeRepository::save)
                .map(cartdb -> {
                    List<CartRecipeCustomize> customs = request.getCustomize().stream()
                            .map(customModel -> customService.updateCustom(cartdb.getId(), customModel))
                            .map(Optional::get)
                            .collect(Collectors.toList());
                    cartdb.setCustomize(customs);

                    return cartdb;
                })
                .map(cartMapper::toModel);
    }
}
