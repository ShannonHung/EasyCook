package com.seminar.easyCookWeb.service.supplier;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.supplier.SupplierMapper;
import com.seminar.easyCookWeb.mapper.supplier.SupplierPersonMapper;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.model.supplier.SupplierPersonModel;
import com.seminar.easyCookWeb.model.user.MemberRequest;
import com.seminar.easyCookWeb.model.user.MemberResponse;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import com.seminar.easyCookWeb.repository.supplier.SupplierPersonRepository;
import com.seminar.easyCookWeb.repository.supplier.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
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

    //Service作用 Model轉成pojo : SupplierPersonModel->SupplierPerson
    /**
     * 透過SupplierPerson Id/Name 尋找合作商聯絡人
     * @param id - 作商聯絡人 id
     * @return 找到的聯絡人結果
     */
    public Optional<SupplierPersonModel> findById(Long id){
        return personRepository.findById(id)
                .map(mapper::toModel);
    }

    //參考IngredientService
    /**
     * 透過id刪除合作商聯絡人
     * @param id 合作商聯絡人id
     * @return
     */
    public Optional<SupplierPersonModel> delete(Long id){
        return personRepository.findById(id)
                .map(it ->{
                    try {
                        personRepository.deleteById(it.getIid());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted " +it.getIid()+ " supplierPerson");
                    }
                })
                .map(mapper::toModel);
    }

    //在Mapper新增了update的方法  WHY?
    public Optional<SupplierPersonModel> update(Long iid, SupplierPersonModel supplierPersonModel) {
        return Optional.of(personRepository.findById(iid))
                .map(it -> {
                    SupplierPerson origin = it.orElseThrow(() -> new EntityNotFoundException("Cannot find SupplierPerson"));
                        mapper.update(supplierPersonModel, origin);
                        return origin;
                    }
                )
                .map(personRepository::save)
                .map(mapper::toModel);
    }

}
