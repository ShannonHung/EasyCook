package com.seminar.easyCookWeb.service.order;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.cart.CartMapper;
import com.seminar.easyCookWeb.mapper.order.CartToOrderCustomMapper;
import com.seminar.easyCookWeb.mapper.order.OrderItemMapper;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.model.order.OrderCart;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import com.seminar.easyCookWeb.pojo.order.OrderItem;
import com.seminar.easyCookWeb.repository.cart.CartRecipeRepository;
import com.seminar.easyCookWeb.repository.order.OrderItemRepository;
import com.seminar.easyCookWeb.repository.order.OrderRepository;
import com.seminar.easyCookWeb.service.cart.CartRecipeService;
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
    private CartRecipeService cartRecipeService;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;
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
     * @param carts
     * @return
     */
    public Optional<List<OrderItem>> saveList(OrderForm orderForm, List<OrderCart> carts){
                 return Optional.of(
                         //先轉乘orderItem List
                         orderItemMapper.carsToOrders(
                                 carts.stream()
                                         .map((cart) -> {
                                             CartRecipeModel cartModel = cartMapper.toModel(cartRecipeRepository.findById(cart.getCartId())
                                                     .orElseThrow(()-> new EntityNotFoundException("Cannot find the cart number " + cart.getCartId())));
                                             cartRecipeRepository.deleteById(cart.getCartId());
                                             //計算該訂單item的價格
                                             cartModel.setSum(cart.getCartSum());
                                             return cartModel;
//                                             if (cartModel.getIsCustomize()) {
//                                                 //DONE if true, sum = handmade cost + every current ingredient price * quantities
//                                                 return cartRecipeService.calculateCustomCartSum(cartModel);
//                                             } else {
//                                                 //DONE if false, sum = recipe price
//                                                 return cartRecipeService.calculateCustomCartSum(cartModel);
//                                             }
                                         })
                                         .collect(Collectors.toList()))
                                 //把List<OrderItem>裡面的每個Item裡面的塞好Form
                 .stream().map((orderItem) -> save(orderItem, orderForm.getId()).get())
                 .collect(Collectors.toList()));
    }
}
