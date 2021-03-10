package com.seminar.easyCookWeb.repository.supplier;

import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    //參考IngredientRepository建置
    @Query("SELECT m FROM Supplier m WHERE m.name LIKE %:title%")
    Optional<List<Supplier>> findByName(@Param("title") String title);

    @Query("SELECT COUNT(i) FROM Supplier i WHERE ( i.name = :name ) AND i.id != :id")
    Long ExistName(@Param("name") String name,  @Param("id") Long id);

    Optional<Supplier> findById(Long id);

}
