package com.seminar.easyCookWeb.repository.ingredient;

import com.seminar.easyCookWeb.pojo.ingredient.Category;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    @Query("SELECT m FROM Ingredient m WHERE m.name LIKE %:title%")
    Optional<List<Ingredient>> findByName(@Param("title") String title);
    Optional<Ingredient> findById(Long id);
    Optional<List<Ingredient>> findAllByCategory(Category category);
    Optional<List<Ingredient>> findAllByCountry(String country);
    Optional<List<Ingredient>> findAllByCountryAndCity(String country, String city);
//    @Query("select i.name from Ingredient i where i.id = :plantId")
//    Boolean deliveryCompleted(Long plantId);

}
