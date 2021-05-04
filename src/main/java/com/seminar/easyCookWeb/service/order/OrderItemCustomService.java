package com.seminar.easyCookWeb.service.order;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.order.OrderItemCustomMapper;
import com.seminar.easyCookWeb.pojo.order.OrderItem;
import com.seminar.easyCookWeb.pojo.order.OrderItemCustom;
import com.seminar.easyCookWeb.repository.order.OrderItemCustomRepository;
import com.seminar.easyCookWeb.repository.order.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemCustomService {
    @Autowired
    private OrderItemCustomRepository itemCustomRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderItemCustomMapper itemCustomMapper;
    @Autowired
    private OrderItemService orderItemService;

    public Optional<OrderItemCustom> save(OrderItemCustom itemCustom, Long orderItemId){
        return Optional.of(itemCustom)
                .map(item -> {
                            item.setOrderItem(orderItemRepository.findById(orderItemId)
                                    .orElseThrow(()-> new EntityNotFoundException("CANNOT FIND ORDERITEM!"+orderItemId)));
                            return item;
                        })
                .map(itemCustomRepository::save);
    }


    public Optional<List<OrderItemCustom>> saveList(List<OrderItemCustom> itemCustoms, Long orderItemId){
        return Optional.of(itemCustoms.stream().map(
                (itemCustom) -> itemCustomRepository.save(save(itemCustom, orderItemId).get()))
                .collect(Collectors.toList()));
    }
}
