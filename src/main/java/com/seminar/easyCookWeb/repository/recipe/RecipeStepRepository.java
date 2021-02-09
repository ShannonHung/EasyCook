package com.seminar.easyCookWeb.repository.recipe;

import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
}
