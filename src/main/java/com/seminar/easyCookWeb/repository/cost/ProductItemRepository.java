package com.seminar.easyCookWeb.repository.cost;

import com.seminar.easyCookWeb.pojo.cost.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
}
