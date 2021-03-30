package com.seminar.easyCookWeb.controller.order;

import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.order.OrderFormModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.pojo.order.OrderForm;
import com.seminar.easyCookWeb.service.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/order" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "訂單Order連接口", description = "提供訂單相關的 Rest API")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @ApiOperation("建立訂單: Create Order {ROLE_MEMBER}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public ResponseEntity<OrderFormModel> create(@Valid @RequestBody OrderFormModel request, Authentication authentication){
        return orderService.create(request, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("CREATE ORDER FAILURE!"));

    }
}
