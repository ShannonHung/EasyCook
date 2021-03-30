package com.seminar.easyCookWeb.repository.supplier;

import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierPersonRepository extends JpaRepository<SupplierPerson, Long> {

    Optional<List<SupplierPerson>> findByName(String name);

}
