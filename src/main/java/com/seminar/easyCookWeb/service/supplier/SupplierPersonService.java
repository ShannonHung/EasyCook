package com.seminar.easyCookWeb.service.supplier;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.supplier.SupplierMapper;
import com.seminar.easyCookWeb.mapper.supplier.SupplierPersonMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.model.supplier.SupplierPersonModel;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import com.seminar.easyCookWeb.repository.supplier.SupplierPersonRepository;
import com.seminar.easyCookWeb.repository.supplier.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierPersonService {
    @Autowired
    SupplierPersonRepository personRepository;
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    SupplierPersonMapper mapper;

    public Optional<SupplierPersonModel> createPerson(Long supplierId, SupplierPersonModel personModel){
        return Optional.of(mapper.toPOJO(personModel))
                .map(pplPojo -> pplPojo.toBuilder()
                        .supplier(
                                supplierRepository.findById(supplierId)
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find recipe"))
                        ).build()
                ).map(personRepository::save)
                .map(mapper::toModel);

    }
}
