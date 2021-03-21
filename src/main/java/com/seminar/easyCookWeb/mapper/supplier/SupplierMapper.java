package com.seminar.easyCookWeb.mapper.supplier;

import com.seminar.easyCookWeb.mapper.recipe.RecipeStepMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.mapstruct.*;

import java.util.List;

//要用SupplierPersonMapper才能轉過去
@Mapper(componentModel = "spring", uses = {SupplierPersonMapper.class})
public interface SupplierMapper {
    SupplierModel toModel(Supplier supplier);

    @Mapping(target = "supplierPeople", ignore = true)
    Supplier toPOJO(SupplierModel supplierModel);

    List<SupplierModel> toModels(List<Supplier> suppliers);

    Iterable<SupplierModel> toIterableModel(Iterable<Supplier> suppliers);

    @Mapping(target = "iid",ignore = true)
    @Mapping(target = "supplierPeople", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(SupplierModel supplierModel, @MappingTarget Supplier supplier);
}
