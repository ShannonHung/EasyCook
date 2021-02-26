package com.seminar.easyCookWeb.mapper.supplier;

import com.seminar.easyCookWeb.mapper.recipe.RecipeStepMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.model.supplier.SupplierPersonModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierPersonMapper {
    SupplierPersonModel toModel(SupplierPerson supplierPerson);

    SupplierPerson toPOJO(SupplierPersonModel supplierPersonModel);

    List<SupplierPersonModel> toModels(List<SupplierPerson> suppliers);
}
