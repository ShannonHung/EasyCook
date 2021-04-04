package com.seminar.easyCookWeb.repository.order;

import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderForm, Long> {
    @Query("SELECT c FROM OrderForm c WHERE ( c.member.id = :id )")
    Optional<List<OrderForm>> findAllByMemberId(@Param("id")Long id);

}
