package com.seminar.easyCookWeb.service.purchase;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.purchase.PurchaseIngredientMapper;
import com.seminar.easyCookWeb.model.purchase.PurchaseIngredientModel;
import com.seminar.easyCookWeb.pojo.purchase.PurchaseIngredient;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.purchase.PurchaseIngredientRepository;
import com.seminar.easyCookWeb.repository.purchase.PurchaseRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseIngredientService {
    @Autowired
    PurchaseIngredientRepository purchaseIngredientRepository;
    @Autowired
    PurchaseRecordRepository purchaseRecordRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    PurchaseIngredientMapper mapper;

    public Optional<PurchaseIngredientModel> create(Long purchaseRecordId,Long ingredientId, PurchaseIngredientModel purchaseIngredientModel){
        return Optional.of(mapper.toPOJO(purchaseIngredientModel))
                .map(it -> it.toBuilder()
                        .purchaseRecord(
                                purchaseRecordRepository.findById(purchaseRecordId)
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find purchaseRecordId"))
                        )
                        .ingredient(
                                ingredientRepository.findById(ingredientId)
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find ingredientId"))
                        )
                        .build()
                )
                .map(purchaseIngredientRepository::save)
                .map((pojo) -> {
                    System.out.println( pojo.toString());
                    return pojo;
                })
                .map(mapper::toModel);
    }

    public Optional<PurchaseIngredientModel> add(PurchaseIngredientModel request) {
        return Optional.of(mapper.toPOJO(request))
                .map(it -> it.toBuilder()
                        .purchaseRecord(
                                purchaseRecordRepository.findById(request.getRecordId())
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find purchaseRecord"))
                        )
                        .ingredient(
                                ingredientRepository.findById(request.getIngredientId())
                                        .orElseThrow(()-> new EntityNotFoundException("Cannot find ingredient"))
                        )
                        .build()
                )
                .map(purchaseIngredientRepository::save)
                .map((pojo) -> {
                    System.out.println(pojo.toString());
                    return pojo;
                })
                .map(mapper::toModel);

    }

    public Optional<Iterable<PurchaseIngredientModel>> findAll() {
        return Optional.of(purchaseIngredientRepository.findAll())
                .map(mapper::toModels);
    }

    public Optional<PurchaseIngredientModel> findById(Long id){
        return purchaseIngredientRepository.findById(id)
                .map(mapper::toModel);
    }

    public Optional<List<PurchaseIngredientModel>> findByRecordId(Long recordId) {
        return Optional.of(purchaseIngredientRepository.findAllByRecordId(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find the supplier!")))
                .map(mapper::toModels);
    }

    /**
     * 透過id刪除合作商聯絡人
     * @param id 合作商聯絡人id
     * @return
     */
    public Optional<PurchaseIngredientModel> delete(Long id){
        return purchaseIngredientRepository.findById(id)
                .map(it ->{
                    try {
                        purchaseIngredientRepository.deleteById(it.getIid());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted " +it.getIid()+ " PurchaseIngredient");
                    }
                })
                .map(mapper::toModel);
    }

    public Optional<PurchaseIngredientModel> update(Long iid,Long purchaseRecordId,Long ingredientId, PurchaseIngredientModel purchaseIngredientModel) {
        return Optional.of(purchaseIngredientRepository.findById(iid))
                .map(it -> {
                    PurchaseIngredient origin = it.orElseThrow(() -> new EntityNotFoundException("Cannot find PurchaseIngredient"));
                            mapper.update(purchaseIngredientModel, origin);
                            return origin;
                        }
                )
                .map(purchaseIngredientRepository::save)
                .map(mapper::toModel);
    }

}
