package com.seminar.easyCookWeb.repository.recipe;

import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {
    Optional<RecipeImage> findById(long id);

    Optional<RecipeImage> findByName(String name);

    @Query("SELECT i FROM RecipeImage i WHERE i.recipe.id like :recipeId")
    Iterable<RecipeImage> findByRecipeId(@Param("recipeId") Long recipeId);
}
