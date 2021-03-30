package com.seminar.easyCookWeb.repository.order;

import com.seminar.easyCookWeb.pojo.order.OrderForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderForm, Long> {
}
