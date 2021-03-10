package com.seminar.easyCookWeb.service.supplier;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.supplier.SupplierMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.model.supplier.SupplierPersonModel;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
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

    public Optional<SupplierModel> findById(Long id){
        return supplierRepository.findById(id)
                .map(mapper::toModel);
    }

    //參考IngredientService
    /**
     * 透過id刪除合作商聯絡人
     * @param id 合作商聯絡人id
     * @return
     */
    public Optional<SupplierModel> delete(Long id){
        return supplierRepository.findById(id)
                .map(it ->{
                    try {
                        supplierRepository.deleteById(it.getIid());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted " +it.getIid()+ " supplier");
                    }
                })
                .map(mapper::toModel);
    }

    //在Mapper新增了update的方法  WHY?
    public Optional<SupplierModel> update(Long iid, SupplierModel supplierModel) {
        return Optional.of(supplierRepository.findById(iid))
                .map(it -> {
                    Supplier origin = it.orElseThrow(() -> new EntityNotFoundException("Cannot find Supplier"));
                    if(supplierRepository.ExistName(supplierModel.getCompanyName(), iid) > 0){
                        throw new EntityCreatedConflictException("this Supplier have already in used!");
                    }else{
                        mapper.update(supplierModel, origin);
                        return origin;
                    }
                })
                .map(supplierRepository::save)
                .map(mapper::toModel);
    }

}
