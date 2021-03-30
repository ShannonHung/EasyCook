package com.seminar.easyCookWeb.mapper.order;

import com.seminar.easyCookWeb.mapper.user.MemberMapper;
import com.seminar.easyCookWeb.model.order.OrderFormModel;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MemberMapper.class, OrderItemMapper.class})
public interface OrderFormMapper {
    OrderFormModel toModel(OrderForm order);

    OrderForm toPOJO(OrderFormModel orderModel);

    List<OrderFormModel> toModels(List<OrderForm> orders);

    Iterable<OrderFormModel> toIterableModel(Iterable<OrderForm> orders);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(OrderFormModel orderModel, @MappingTarget OrderForm orders);

}
