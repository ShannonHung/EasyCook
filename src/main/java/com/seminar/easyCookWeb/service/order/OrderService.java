package com.seminar.easyCookWeb.service.order;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.order.OrderFormMapper;
import com.seminar.easyCookWeb.mapper.order.OrderItemMapper;
import com.seminar.easyCookWeb.model.order.OrderFormModel;
import com.seminar.easyCookWeb.model.order.update.OrderUpdateEmployee;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import com.seminar.easyCookWeb.repository.cart.CartRecipeRepository;
import com.seminar.easyCookWeb.repository.order.OrderRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderFormMapper orderMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CartRecipeRepository cartRepository;



    DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public String createOrderNumber(){
        return "OR"+ (System.currentTimeMillis() / 1000);
    }

    public Optional<OrderFormModel> create(OrderFormModel request, Authentication auth) {
        return Optional.of(orderMapper.toPOJO(request))
                .map((pojo) -> {
                    return pojo.toBuilder()
                            .orderNumber(createOrderNumber())
                            .member(memberRepository.findByAccount(auth.getName()).get())
                            .orderTime(OffsetDateTime.parse(OffsetDateTime.now().format(dtf)))
                            .build();
                })
                .map(orderRepository::save)
                .map((pojo) -> {
                    //cart轉成orderItem並且塞好
                    pojo.setOrderItems(orderItemService.saveList(pojo, request.getCartId()).get());
                    return pojo;

//                    List<CartRecipe> carts = new LinkedList<>();
//                    List<OrderItem> orderItems = new LinkedList<>();
//
//                    request.getCartId().forEach((cartId) -> {
//                        carts.add(cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cannot find the shopping cartId " + cartId)));
//                    });
//
//                    orderItems = orderItemMapper.carsToOrders(carts).stream().map(
//                            (cart) -> {
//                                cart.setOrderForm(pojo);
//                                return cart;
//                            }
//                    ).collect(Collectors.toList());
//
//                    pojo.setOrderItems(orderItems);
//                    return pojo;
                }).map(orderRepository::save)
                .map(orderMapper::toModel);
    }

    public Optional<List<OrderFormModel>> getAll(Authentication auth){
       Member member = memberRepository.findByAccount(auth.getName()).orElseThrow(()-> new EntityNotFoundException("Cannot find user, or you are not the member"));

        return Optional.of(orderRepository.findAllByMemberId(member.getId())
                .map(orderMapper::toModels)
                .orElseThrow(()-> new EntityNotFoundException("CANNOT FIND ANY ORDERS")));
    }

    public Optional<List<OrderFormModel>> getAll(){
        return Optional.of(orderRepository.findAll())
                .map(orderMapper::toModels)
                .map(Optional::of)
                .orElseThrow(()-> new EntityNotFoundException("CANNOT FIND ANY ORDERS"));
    }

    public Optional<OrderFormModel> deleteById(Long orderId){
        OrderForm deleteItem = orderRepository.findById(orderId).orElseThrow(()-> new EntityNotFoundException("CANNOT FIND ORDER! id "+orderId));
        orderRepository.deleteById(orderId);
        return Optional.of(deleteItem)
                .map(orderMapper::toModel);
    }

    public Optional<OrderFormModel> findById(Long orderId){
        return Optional.of(orderRepository.findById(orderId))
                .map(Optional::get)
                .map(orderMapper::toModel);
    }

    /**
     * for Member update api, only can update status
     * @param orderId
     * @param auth
     * @return
     */
    public Optional<OrderFormModel> updateById(Long orderId, Authentication auth){
        OrderForm origin = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("CANNOT FIND ORDER! ID "+orderId));
        if(!origin.getMember().getAccount().equals(auth.getName())) throw new EntityNotFoundException("YOU ARE NOT THIS ORDER'S OWNER! ID "+orderId);
        if(origin.getStatus().equals("尚未確認")) {
            origin.setStatus("已取消");
        }else{
            throw new BusinessException("SORRY! CANNOT CHANGE THE STATUS, YOUR ORDER IS BEING PREPARED.");
        }
        return Optional.of(origin)
                .map(orderRepository::save)
                .map(orderMapper::toModel);

    }

    /**
     * for Employee update api, only update everything except item or itemcustom
     * @param orderUpdateEmployee
     * @param orderId
     * @return
     */
    public Optional<OrderFormModel> updateById(OrderUpdateEmployee orderUpdateEmployee, Long orderId){
        OrderForm origin = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("CANNOT FIND ORDER! ID "+orderId));
        return Optional.of(origin)
                .map((db) -> {
                    orderMapper.update(orderUpdateEmployee, db);
                    return db;
                }).map(orderRepository::save)
                .map(orderMapper::toModel);

    }

}
