package com.seminar.easyCookWeb.repository.users;

import com.seminar.easyCookWeb.pojo.appUser.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByAccount(String account);
    Optional<Employee> findById(Long id);
}
