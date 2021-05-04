package com.seminar.easyCookWeb.repository.order;

import com.seminar.easyCookWeb.pojo.order.OrderItemCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemCustomRepository extends JpaRepository<OrderItemCustom, Long> {
}
