package com.seminar.easyCookWeb.service.cart;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.cart.CartMapper;
import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.repository.cart.CartRecipeCustomizeRepository;
import com.seminar.easyCookWeb.repository.cart.CartRecipeRepository;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import com.seminar.easyCookWeb.service.cost.HandmadeService;
import com.seminar.easyCookWeb.service.ingredient.IngredientService;
import com.seminar.easyCookWeb.service.recipe.RecipeImageService;
import com.seminar.easyCookWeb.service.recipe.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CartRecipeService {
    @Autowired
    private CartRecipeRepository cartRecipeRepository;
    @Autowired
    private CartRecipeCustomizeRepository customizeRepository;
    @Autowired
    private CartRecipeCustomService customService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeImageService recipeImageService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private HandmadeService handmadeService;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private IngredientMapper ingredientMapper;
    @Autowired
    private CartMapper cartMapper;

    public Optional<CartRecipeModel> addByCustomize(CartRecipeRequest request, Authentication authentication) {

        return Optional.of(request)
                .map((re) -> {
                    request.setIsCustomize(true);
                    Member member = memberRepository.findByAccount(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("Cannot find the user, maybe you are not member."));
                    return cartRecipeRepository.save(CartRecipe.builder()
                            .member(member)
                            .IsCustomize(re.getIsCustomize())
                            .recipe(recipeRepository.findById(request.getRecipeId())
                                    .orElseThrow(() -> new EntityNotFoundException("Cannot find this recipe")))
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
                                .orElseThrow(() -> new EntityNotFoundException("Cannot find " + cus.getIngredientId() + " this ingredient")));
                        cust = customizeRepository.save(cust);
                        customize.add(cust);
                    });

                    cart.setCustomize(customize);
                    return cartRecipeRepository.save(cart);
                })
                .map((cart) -> {
                    CartRecipeModel model = cartMapper.toModel(cart);
                    model.setRecipeImage(recipeImageService.getFirstImageByRecipeId(model.getRecipe().getId()).get());
                    return model;
                })
                .map(this::calculateCustomCartSum);
    }

    public Optional<CartRecipeModel> addByDefault(Long recipeId, Authentication authentication) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("CANNOT FIND THIS RECIPE! ID " + recipeId));
        CartRecipe cartRecipe = cartMapper.DefaultRecipeToCart(recipe);
        cartRecipe.setIsCustomize(false);

        return Optional.of(cartRecipe)
                .map((re) -> {
                    Member member = memberRepository.findByAccount(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("Cannot find the user, maybe you are not member."));
                    return cartRecipeRepository.save(CartRecipe.builder()
                            .member(member)
                            .IsCustomize(re.getIsCustomize())
                            .recipe(recipe).build());
                })
                .map((cart) -> {
                    List<CartRecipeCustomize> customize = new LinkedList<>();

                    cartRecipe.getCustomize().forEach((cus) -> {
                        CartRecipeCustomize cust = CartRecipeCustomize.builder()
                                .quantityRequired(cus.getQuantityRequired())
                                .cartRecipe(cart)
                                .build();
                        cust.setIngredient(ingredientService.readByIngredientId(cus.getIngredient().getId())
                                .map(ingredientMapper::toPOJO)
                                .orElseThrow(() -> new EntityNotFoundException("CANNOT FIND THE INGREDIENT! ID " + cus.getIngredient().getId())));
                        cust = customizeRepository.save(cust);
                        customize.add(cust);
                    });

                    cart.setCustomize(customize);
                    return cartRecipeRepository.save(cart);
                })
                .map((cart) -> {
                    CartRecipeModel model = cartMapper.toModel(cart);
                    model.setRecipeImage(recipeImageService.getFirstImageByRecipeId(model.getRecipe().getId()).get());
                    return model;
                })
                .map(this::calculateDefaultCartSum);
    }

    public Optional<CartRecipeModel> getCartById(long cartId) {
        return cartRecipeRepository.findById(cartId)
                .map(cartMapper::toModel)
                .map((model) -> {
                    model.setRecipeImage(recipeImageService.getFirstImageByRecipeId(model.getRecipe().getId()).get());
                    return model;
                })
                .map(this::calculateDefaultCartSum);
    }

    /**
     * 取得特定消費者的購物車清單 並且 計算所有cart的單價
     * 以isCustomize做區分
     *
     * @param authentication 使用者資訊
     * @return 已經做好sum的carts
     */
    public Optional<Iterable<CartRecipeModel>> findAllByMember(Authentication authentication) {
        Member member = memberRepository.findByAccount(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("Cannot find user, or you are not the member"));

        return cartRecipeRepository.findAllByMemberId(member.getId())
                .map(cartMapper::toIterableModel)
                .map((models) -> {
                    //DONE 判斷isCustomize = true or false
                    return StreamSupport.stream(models.spliterator(), false)
                            .map((cart) -> {
                                cart.setRecipeImage(recipeImageService.getFirstImageByRecipeId(cart.getRecipe().getId()).get());
                                cart.setRecipe(recipeService.setRecipeOutOfStackIngredients(cart.getRecipe()).get());
                                if (cart.getIsCustomize()) {
                                    //DONE if true, sum = handmade cost + every current ingredient price * quantities
                                    return calculateCustomCartSum(cart);
                                } else {
                                    //DONE if false, sum = recipe price
                                    return calculateDefaultCartSum(cart);
                                }
                            }).collect(Collectors.toList());
                });

    }

    /**
     * 如果isCustomize = true
     *
     * @param cartRecipeModel 要進行計算的 per cart
     * @return 已經計算好的cartRecipeModel sum
     */
    public CartRecipeModel calculateCustomCartSum(CartRecipeModel cartRecipeModel) {
        AtomicReference<Double> sum = new AtomicReference<>(0D);

        //DONE 1. 取得目前的人工價格
        sum.updateAndGet(v -> v + handmadeService.getInit().get().getCost());

        //DONE 2. 取得每個ingredient的價格並且更新sum
        cartRecipeModel.getCustomize().forEach((customModel -> {
            double ingredientSum = customModel.getQuantityRequired() * customModel.getIngredient().getPrice();
            sum.updateAndGet(v -> v + ingredientSum);
        }));

        cartRecipeModel.setSum(sum.get());
        return cartRecipeModel;

    }

    /**
     * 如果isCustomize = false 設定sum為 recipePrice
     *
     * @param cartRecipeModel
     * @return 已經計算好的cartRecipeModel sum
     */
    public CartRecipeModel calculateDefaultCartSum(CartRecipeModel cartRecipeModel) {
        return cartRecipeModel.toBuilder()
                .sum(cartRecipeModel.getRecipe().getPrice()).build();
    }

    public Optional<CartRecipeModel> deleteById(Long id, Authentication authentication) {

        CartRecipe model = cartRecipeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cannot find this cart item"));
        if (!authentication.getName().equals(model.getMember().getAccount())) {
            throw new BusinessException("These cart's owner is not you, so you cannot delete this cart item");
        }
        cartRecipeRepository.deleteById(id);
        return Optional.of(model)
                .map(cartMapper::toModel)
                .map((model1) -> {
                    model1.setRecipeImage(recipeImageService.getFirstImageByRecipeId(model1.getRecipe().getId()).get());
                    return model1;
                });
    }

    public Optional<CartRecipeModel> update(Long cartId, CartRecipeModel request, Authentication authentication) {
        CartRecipe origin = cartRecipeRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("cannot find this cart item"));
        checkAuth(authentication, origin.getMember().getAccount());
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
                .map(cartMapper::toModel)
                .map((model) -> {
                    model.setRecipeImage(recipeImageService.getFirstImageByRecipeId(model.getRecipe().getId()).get());
                    if(model.getIsCustomize())return calculateCustomCartSum(model);
                    else return calculateDefaultCartSum(model);
                });
    }
    public void checkAuth(Authentication authentication, String owner){
        if (!authentication.getName().equals(owner)) {
            throw new BusinessException("These cart's owner is not you, so you don't have authority to access");
        }
    }
}
