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

    //參考Recipe建置但會錯
    //參考IngredientRepository建置
    Optional<List<SupplierPerson>> findByName(String title);

}
