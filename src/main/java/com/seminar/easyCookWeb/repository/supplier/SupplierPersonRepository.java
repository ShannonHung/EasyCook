package com.seminar.easyCookWeb.repository.supplier;

import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierPersonRepository extends JpaRepository<SupplierPerson, Long> {
}
