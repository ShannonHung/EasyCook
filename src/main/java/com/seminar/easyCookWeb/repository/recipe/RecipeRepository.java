package com.seminar.easyCookWeb.repository.recipe;

import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT m FROM Recipe m WHERE m.name LIKE :title")
    Optional<Iterable<Recipe>> findByName(@Param("title") String title);

    @Query("SELECT m FROM Recipe m WHERE m.name LIKE %:title%")
    Optional<Iterable<Recipe>> findByPartName(@Param("title") String title);


}
