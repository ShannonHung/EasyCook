package com.seminar.easyCookWeb.repository.users;

import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByAccount(String account);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findById(Long id);
    @Query("SELECT COUNT(e) FROM Employee e WHERE ( e.account = :account OR e.email = :email ) AND e.id != :id")
    Long ExistAccountandEmail(@Param("account") String account, @Param("email") String email, @Param("id") Long id);

}
