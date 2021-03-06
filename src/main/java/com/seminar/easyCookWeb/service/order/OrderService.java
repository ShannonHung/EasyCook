package com.seminar.easyCookWeb.service.order;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.order.OrderFormMapper;
import com.seminar.easyCookWeb.mapper.order.OrderItemMapper;
import com.seminar.easyCookWeb.model.order.OrderFormModel;
import com.seminar.easyCookWeb.model.order.update.OrderUpdateEmployee;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import com.seminar.easyCookWeb.pojo.order.OrderItem;
import com.seminar.easyCookWeb.pojo.order.OrderItemCustom;
import com.seminar.easyCookWeb.repository.cart.CartRecipeRepository;
import com.seminar.easyCookWeb.repository.order.OrderRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import com.seminar.easyCookWeb.service.recipe.RecipeImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    private RecipeImageService recipeImageService;
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
                    pojo.setOrderItems(orderItemService.saveList(pojo, request.getCarts()).get());
                    return pojo;
                }).map(orderRepository::save)
                .map(orderMapper::toModel);
    }

    public Optional<List<OrderFormModel>> getAll(Authentication auth){
       Member member = memberRepository.findByAccount(auth.getName()).orElseThrow(()-> new EntityNotFoundException("Cannot find user, or you are not the member"));

        return Optional.of(orderRepository.findAllByMemberId(member.getId())
                .map(orderMapper::toModels)
                .map((lists) -> StreamSupport.stream(lists.spliterator(), false)
                            .map(this::setFirstRecipeImage).collect(Collectors.toList())
                )
                .orElseThrow(()-> new EntityNotFoundException("CANNOT FIND ANY ORDERS")));
    }


    public Optional<List<OrderFormModel>> getAll(){
        return Optional.of(orderRepository.findAll())
                .map(orderMapper::toModels)
                .map((lists) -> StreamSupport.stream(lists.spliterator(), false)
                        .map(this::setFirstRecipeImage).collect(Collectors.toList())
                )
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
                .map(orderMapper::toModel)
                .map(this::setFirstRecipeImage);
    }

    public OrderFormModel setFirstRecipeImage(OrderFormModel order){
        order.getOrderItems().forEach((cart) -> {
            cart.setRecipeImage(recipeImageService.getFirstImageByRecipeId(cart.getRecipe().getId()).orElseThrow(()-> new EntityNotFoundException("Cannot find the recipe image for orderItem")));
        });
        return order;
    }

    public Optional<OrderFormModel> findByOrderNumber(String orderNumber){
        return Optional.of(orderRepository.findByOrderNumber(orderNumber))
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
        if(origin.getStatus().equals("toConfirm")) {
            origin.setStatus("canceled");
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
