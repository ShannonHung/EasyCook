package com.seminar.easyCookWeb.repository.supplier;

import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierPersonRepository extends JpaRepository<SupplierPerson, Long> {

//    //參考Recipe建置但會錯
//    @Query("SELECT m FROM SupplierContactPerson m WHERE m.name LIKE :title")
//    Optional<Iterable<SupplierPerson>> findByName(@Param("title") String title);
//
//    @Query("SELECT m FROM SupplierContactPerson m WHERE m.name LIKE %:title%")
//    Optional<Iterable<SupplierPerson>> findByPartName(@Param("title") String title);

    //參考IngredientRepository建置
    @Query("SELECT m FROM SupplierContactPerson m WHERE m.name LIKE %:title%")
    Optional<List<SupplierPerson>> findByName(@Param("title") String title);

    @Query("SELECT COUNT(i) FROM SupplierContactPerson i WHERE ( i.name = :name ) AND i.id != :id")
    Long ExistName(@Param("name") String name,  @Param("id") Long id);

    Optional<SupplierPerson> findById(Long id);
}
