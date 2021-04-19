package com.seminar.easyCookWeb.repository.cost;

import com.seminar.easyCookWeb.pojo.cost.HandmadeCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HandmadeRepository extends JpaRepository<HandmadeCost, Long> {
    @Query("SELECT i FROM HandmadeCost i WHERE ( i.name = 'initial')")
    Optional<HandmadeCost> findInitCost();

}
