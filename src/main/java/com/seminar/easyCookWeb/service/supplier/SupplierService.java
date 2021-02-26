package com.seminar.easyCookWeb.service.supplier;

import com.seminar.easyCookWeb.mapper.supplier.SupplierMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.repository.supplier.SupplierPersonRepository;
import com.seminar.easyCookWeb.repository.supplier.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierPersonRepository personRepository;
    @Autowired
    private SupplierPersonService personService;
    @Autowired
    private SupplierMapper mapper;

    public Optional<SupplierModel> createSupplier(SupplierModel supplierModel){
        return Optional.of(mapper.toPOJO(supplierModel))
                .map(supplierRepository::save)
                .map(mapper::toModel)
                .map(model -> {
                    return model.toBuilder()
                            .supplierPeople(supplierModel.getSupplierPeople().stream()
                                            .map(pplModel -> personService.createPerson(model.getIid(), pplModel))
                                            .map(Optional::get)
                                            .collect(Collectors.toList())
                            ).build();
                });
    }
}
