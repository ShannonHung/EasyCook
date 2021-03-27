package com.seminar.easyCookWeb.repository.purchase;


import com.seminar.easyCookWeb.pojo.purchase.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, Long> {

//    Optional<List<PurchaseRecord>> findBySupplierId(String supplierId);
}
