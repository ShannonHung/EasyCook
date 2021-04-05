package com.seminar.easyCookWeb.mapper.cost;

import com.seminar.easyCookWeb.model.cost.HandmadeModel;
import com.seminar.easyCookWeb.model.order.update.OrderUpdateEmployee;
import com.seminar.easyCookWeb.pojo.cost.HandmadeCost;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ProductItemMapper.class})
public interface HandmadeMapper {
    HandmadeModel toModel(HandmadeCost handmadeCost);

    HandmadeCost toPojo(HandmadeModel handmadeModel);

    @Mapping(target = "id",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(HandmadeModel handmadeModel, @MappingTarget HandmadeCost handmadeCost);

}
