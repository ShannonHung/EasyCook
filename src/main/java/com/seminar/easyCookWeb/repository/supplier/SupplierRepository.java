package com.seminar.easyCookWeb.repository.supplier;

import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
