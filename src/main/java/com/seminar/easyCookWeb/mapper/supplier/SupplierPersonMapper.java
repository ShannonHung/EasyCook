package com.seminar.easyCookWeb.mapper.supplier;

import com.seminar.easyCookWeb.model.supplier.SupplierPersonModel;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierPersonMapper {
    SupplierPersonModel toModel(SupplierPerson supplierPerson);

    @Mapping(target = "iid",ignore = true)
    SupplierPerson toPOJO(SupplierPersonModel supplierPersonModel);

    List<SupplierPersonModel> toModels(List<SupplierPerson> suppliers);

//    @Mapping(target = "supplierPeople", ignore = true)
    @Mapping(target = "iid",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(SupplierPersonModel supplierPersonModel, @MappingTarget SupplierPerson supplierPerson);
}
