package com.seminar.easyCookWeb.repository.recipe;

import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
