package com.seminar.easyCookWeb.service.order;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.order.CartToOrderCustomMapper;
import com.seminar.easyCookWeb.mapper.order.OrderItemMapper;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import com.seminar.easyCookWeb.pojo.order.OrderItem;
import com.seminar.easyCookWeb.repository.cart.CartRecipeRepository;
import com.seminar.easyCookWeb.repository.order.OrderItemRepository;
import com.seminar.easyCookWeb.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRecipeRepository cartRecipeRepository;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderItemCustomService itemCustomService;
    /**
     * 把每個OderItem塞好Form
     * Form塞好Custom
     * @param orderItem
     * @param OrderFromId
     * @return
     */
    public Optional<OrderItem> save(OrderItem orderItem, Long OrderFromId){
        return Optional.of(orderItem)
                .map((item) ->
                        //針對每個item塞好form
                        item.toBuilder()
                            .orderForm(orderRepository.findById(OrderFromId).get())
                            .build()
                )
                .map(orderItemRepository::save)
                //針對每個Item塞好Custom
                .map(db -> db.toBuilder()
                            .customize(itemCustomService.saveList(
                                    orderItem.getCustomize(), db.getId()).get())
                            .build()
                );
    }

    /**
     * 目的在幫我把資料都塞好
     * @param orderForm
     * @param cartIds
     * @return
     */
    public Optional<List<OrderItem>> saveList(OrderForm orderForm, List<Long> cartIds){
                 return Optional.of(
                         //先轉乘orderItem List
                         orderItemMapper.carsToOrders(
                                 cartIds.stream()
                                         .map((cartId) -> {
                                             CartRecipe cart = cartRecipeRepository.findById(cartId).orElseThrow(()-> new EntityNotFoundException("Cannot find the cart number " + cartId));
                                             cartRecipeRepository.deleteById(cartId);
                                             return cart;
                                         })
                                         .collect(Collectors.toList()))
                                 //把List<OrderItem>裡面的每個Item裡面的塞好Form
                 .stream().map((orderItem) -> save(orderItem, orderForm.getId()).get())
                 .collect(Collectors.toList()));

    }


}
