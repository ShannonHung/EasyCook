package com.seminar.easyCookWeb.repository.cart;

import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRecipeCustomizeRepository extends JpaRepository<CartRecipeCustomize, Long> {
}
