package com.seminar.easyCookWeb.service.purchase;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.purchase.PurchaseRecordMapper;
import com.seminar.easyCookWeb.model.cart.request.CartRecipeRequest;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.model.purchase.PurchaseRecordModel;
import com.seminar.easyCookWeb.model.supplier.SupplierPersonModel;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import com.seminar.easyCookWeb.pojo.purchase.PurchaseRecord;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import com.seminar.easyCookWeb.repository.purchase.PurchaseRecordRepository;
import com.seminar.easyCookWeb.repository.supplier.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseRecordService {
    @Autowired
    PurchaseRecordRepository purchaseRecordRepository;
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    PurchaseRecordMapper mapper;

    public Optional<PurchaseRecordModel> create(Long supplierId, PurchaseRecordModel purchaseRecordModel){
        return Optional.of(mapper.toPOJO(purchaseRecordModel))
                .map(it -> it.toBuilder()
                        .supplier(
                                supplierRepository.findById(supplierId)
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find supplier"))
                        )
                        .build()
                )
                .map(purchaseRecordRepository::save)
                .map((pojo) -> {
                    System.out.println( pojo.toString());
                    return pojo;
                })
                .map(mapper::toModel);
    }

    public Optional<PurchaseRecordModel> add(PurchaseRecordModel request) {
        return Optional.of(mapper.toPOJO(request))
                .map(it -> it.toBuilder()
                        .supplier(
                                supplierRepository.findById(request.getSupplierId())
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find supplier"))
                        )
                        .build()
                )
                .map(purchaseRecordRepository::save)
                .map((pojo) -> {
                    System.out.println( pojo.toString());
                    return pojo;
                })
                .map(mapper::toModel);

    }

    public Optional<Iterable<PurchaseRecordModel>> findAll() {
        return Optional.of(purchaseRecordRepository.findAll())
                .map(mapper::toModels);
    }

    //Service作用 Model轉成pojo : SupplierPersonModel->SupplierPerson
    public Optional<PurchaseRecordModel> findById(Long id){
        return purchaseRecordRepository.findById(id)
                .map(mapper::toModel);
    }

    public Optional<List<PurchaseRecordModel>> findBySupplierId(Long supplierId) {
        //test
        return Optional.of(purchaseRecordRepository.findAllBySupplierId(supplierId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find the supplier!")))
                .map(mapper::toModels);
    }

    /**
     * 透過id刪除合作商聯絡人
     * @param id 合作商聯絡人id
     * @return
     */
    public Optional<PurchaseRecordModel> delete(Long id){
        return purchaseRecordRepository.findById(id)
                .map(it ->{
                    try {
                        purchaseRecordRepository.deleteById(it.getIid());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted " +it.getIid()+ " purchaseRecord");
                    }
                })
                .map(mapper::toModel);
    }

    public Optional<PurchaseRecordModel> update(Long iid, PurchaseRecordModel purchaseRecordModel) {
        return Optional.of(purchaseRecordRepository.findById(iid))
                .map(it -> {
                            PurchaseRecord origin = it.orElseThrow(() -> new EntityNotFoundException("Cannot find PurchaseRecord"));
                            mapper.update(purchaseRecordModel, origin);
                            return origin;
                        }
                )
                .map(purchaseRecordRepository::save)
                .map(mapper::toModel);
    }

}
