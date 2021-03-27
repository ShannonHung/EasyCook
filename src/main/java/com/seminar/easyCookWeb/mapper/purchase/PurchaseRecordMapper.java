package com.seminar.easyCookWeb.mapper.purchase;

import com.seminar.easyCookWeb.model.purchase.PurchaseRecordModel;
import com.seminar.easyCookWeb.pojo.purchase.PurchaseRecord;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseRecordMapper {
    PurchaseRecordModel toModel(PurchaseRecord purchaseRecord);

    @Mapping(target = "iid",ignore = true)
    PurchaseRecord toPOJO(PurchaseRecordModel purchaseRecordModel);

    List<PurchaseRecordModel> toModels(List<PurchaseRecord> purchaseRecords);

    @Mapping(target = "iid",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(PurchaseRecordModel purchaseRecordModel, @MappingTarget PurchaseRecord purchaseRecord);
}
