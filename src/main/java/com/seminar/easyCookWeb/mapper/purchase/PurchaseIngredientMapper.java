package com.seminar.easyCookWeb.mapper.purchase;

import com.seminar.easyCookWeb.model.purchase.PurchaseIngredientModel;
import com.seminar.easyCookWeb.pojo.purchase.PurchaseIngredient;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring")
public interface PurchaseIngredientMapper{
    PurchaseIngredientModel toModel(PurchaseIngredient purchaseIngredient);

    @Mapping(target = "iid",ignore = true)
    PurchaseIngredient toPOJO(PurchaseIngredientModel purchaseIngredientModel);

    List<PurchaseIngredientModel> toModels(List<PurchaseIngredient> purchaseIngredient);

    @Mapping(target = "iid",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(PurchaseIngredientModel purchaseIngredientModel, @MappingTarget PurchaseIngredient purchaseIngredient);
}
