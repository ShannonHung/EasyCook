package com.seminar.easyCookWeb.repository.order;

import com.seminar.easyCookWeb.pojo.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
