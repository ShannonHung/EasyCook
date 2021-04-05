package com.seminar.easyCookWeb.repository.cost;

import com.seminar.easyCookWeb.pojo.cost.HandmadeCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandmadeRepository extends JpaRepository<HandmadeCost, Long> {
}
