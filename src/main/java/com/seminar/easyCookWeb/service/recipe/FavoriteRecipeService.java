package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.favorite.FavoriteRecipeModel;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.recipe.FavoriteRecipe;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.repository.recipe.FavoriteRecipeRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class FavoriteRecipeService {
    @Autowired
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeImageService recipeImageService;
    @Autowired
    private RecipeMapper recipeMapper;

    public Optional<List<RecipeModel>> create(Long recipeId, Authentication authentication) {
        Member member = memberRepository.findByAccount(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("CANNOT FIND MEMBER"));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("CANNOT FIND THE RECIPE. id = " + recipeId));

        if(favoriteRecipeRepository.checkHaveFavorite(member.getId(), recipeId).isEmpty()){
            return Optional.of(
                    new FavoriteRecipe().toBuilder()
                            .member(member)
                            .recipe(recipe)
                            .build()
            )
                    .map(favoriteRecipeRepository::save)
                    .map((f) -> {
                        return getAllFavorite(authentication).get();
                    });
        }else {
            throw new EntityCreatedConflictException("THIS RECIPE HAVE ALREADY IN YOUR FAVORITES");
        }


    }


    public Optional<List<RecipeModel>> getAllFavorite(Authentication authentication) {
        Member member = memberRepository.findByAccount(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("CANNOT FIND MEMBER"));
        List<RecipeModel> recipeModels = new LinkedList<>();

        favoriteRecipeRepository.findByMemberId(member.getId()).orElseThrow(() -> new EntityNotFoundException("CANNOT FIND ANY RECIPE IN YOUR FAVORITE"))
                .forEach((favoriteRecipe) ->
                    recipeModels.add(setRecipeImage(recipeMapper.toModel(favoriteRecipe.getRecipe())))
                );
        return Optional.of(recipeModels);
    }

    public Optional<List<RecipeModel>> removeRecipe(Long recipeId, Authentication authentication) {
        Member member = memberRepository.findByAccount(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("CANNOT FIND MEMBER"));
        if(favoriteRecipeRepository.checkHaveFavorite(member.getId(), recipeId).isEmpty()) {
            throw new EntityNotFoundException("CANNOT FIND THE RECIPE IN YOUR FAVORITES");
        } else {
            favoriteRecipeRepository.removeRecipe(member.getId(), recipeId);
            return getAllFavorite(authentication);

        }
    }

    public RecipeModel setRecipeImage(RecipeModel model) {
        model.setRecipeImage(recipeImageService.getFirstImageByRecipeId(model.getId()).get());
        return model;
    }


}
