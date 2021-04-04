package com.seminar.easyCookWeb.repository.purchase;

import com.seminar.easyCookWeb.pojo.purchase.PurchaseIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseIngredientRespository extends JpaRepository<PurchaseIngredient,Long> {

    @Query("SELECT i FROM PurchaseIngredient i WHERE i.purchaseRecord.id = :id")
    Optional<List<PurchaseIngredient>> findAllByRecordId(@Param("id") Long id);
}
