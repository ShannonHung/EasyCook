package com.seminar.easyCookWeb.repository.recipe;

import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

    @Query("SELECT m FROM RecipeStep m WHERE m.recipe.id = ?1 order by m.startTime")
    Optional<List<RecipeStep>> findByRecipeId(long id);

}
