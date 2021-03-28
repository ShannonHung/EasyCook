package com.seminar.easyCookWeb.repository.cart;

import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRecipeRepository extends JpaRepository<CartRecipe, Long> {
    @Query("SELECT c FROM CartRecipe c WHERE ( c.member.id = :id )")
    Optional<Iterable<CartRecipe>> findAllByMemberId(@Param("id") Long id);

}
