package com.seminar.easyCookWeb.repository.recipe;

import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.FavoriteRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipe, Long> {
    @Query("SELECT m FROM FavoriteRecipe m WHERE m.member.id = ?1")
    Optional<List<FavoriteRecipe>> findByMemberId(Long id);

    @Query("SELECT m FROM FavoriteRecipe m WHERE m.member.id = ?1 and m.recipe.id = ?2")
    Optional<FavoriteRecipe> checkHaveFavorite(Long memberId, Long recipeId);

    @Transactional
    @Modifying
    @Query("delete from FavoriteRecipe m where m.member.id = ?1 and m.recipe.id=?2")
    void removeRecipe(Long memberId, Long recipeId);

}
