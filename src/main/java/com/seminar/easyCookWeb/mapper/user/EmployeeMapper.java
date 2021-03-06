package com.seminar.easyCookWeb.mapper.user;

import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper MAPPER = Mappers.getMapper( EmployeeMapper.class );

    EmployeeResponse toModel(Employee employee);

    Employee toPOJO(EmployeeRequest request);
    Employee toPOJO(EmployeeResponse request);

    List<EmployeeResponse> toModels(List<Employee> employees);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "role",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(EmployeeRequest employeeRequest, @MappingTarget Employee employee);

}
