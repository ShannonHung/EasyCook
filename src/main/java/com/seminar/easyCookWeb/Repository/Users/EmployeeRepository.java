package com.seminar.easyCookWeb.Repository.Users;

import com.seminar.easyCookWeb.Pojo.app_user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByAccount(String account);
    Optional<Employee> findById(Long id);
}
