package com.seminar.easyCookWeb.repository.recipe;

import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {
    Optional<RecipeImage> findById(long id);
}
