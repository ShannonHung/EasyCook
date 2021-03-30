package com.seminar.easyCookWeb.service.order;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.order.OrderFormMapper;
import com.seminar.easyCookWeb.mapper.order.OrderItemMapper;
import com.seminar.easyCookWeb.model.order.OrderFormModel;
import com.seminar.easyCookWeb.pojo.cart.CartRecipe;
import com.seminar.easyCookWeb.pojo.order.OrderItem;
import com.seminar.easyCookWeb.repository.cart.CartRecipeRepository;
import com.seminar.easyCookWeb.repository.order.OrderRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private CartRecipeRepository cartRepository;


    DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public Optional<OrderFormModel> create(OrderFormModel request, Authentication auth) {
        return Optional.of(orderMapper.toPOJO(request))
                .map((pojo) -> {
                    return pojo.toBuilder()
                            .member(memberRepository.findByAccount(auth.getName()).get())
                            .orderTime(OffsetDateTime.parse(OffsetDateTime.now().format(dtf)))
                            .build();
                })
                .map(orderRepository::save)
                .map((pojo) -> {
                    List<CartRecipe> carts = new LinkedList<>();
                    List<OrderItem> orderItems = new LinkedList<>();

                    request.getCartId().forEach((cartId) -> {
                        carts.add(cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cannot find the shopping cartId " + cartId)));
                    });

                    orderItems = orderItemMapper.carsToOrders(carts).stream().map(
                            (cart) -> {
                                cart.setOrderForm(pojo);
                                return cart;
                            }
                    ).collect(Collectors.toList());

                    pojo.setOrderItems(orderItems);
                    return pojo;
                }).map(orderRepository::save)
                .map(orderMapper::toModel);
    }
}
