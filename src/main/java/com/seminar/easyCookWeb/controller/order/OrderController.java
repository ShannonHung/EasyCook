package com.seminar.easyCookWeb.controller.order;

import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.order.OrderFormModel;
import com.seminar.easyCookWeb.model.order.update.OrderUpdateEmployee;
import com.seminar.easyCookWeb.service.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/order" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "訂單Order連接口", description = "提供訂單相關的 Rest API")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @ApiOperation("建立訂單: Create Order {ROLE_MEMBER, 'ROLE_VIP'}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<OrderFormModel> create(@Valid @RequestBody OrderFormModel request, Authentication authentication){
        return orderService.create(request, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("CREATE ORDER FAILURE!"));
    }

    @GetMapping("/all")
    @ApiOperation("取得使用者所有訂單: Get All Orders {ROLE_MEMBER, 'ROLE_VIP'}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<List<OrderFormModel>> getAll(Authentication auth){
        return orderService.getAll(auth)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("GET ORDERS FAILURE!"));
    }

    @GetMapping("/employee/all")
    @ApiOperation("取得所有訂單: Get All Orders {ROLE_MEMBER}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<OrderFormModel>> getAllForEmployee(){
        return orderService.getAll()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("GET ORDERS FAILURE!"));
    }



    @GetMapping("/{orderId}")
    @ApiOperation("取得訂單透過訂單編號: Get Order By Id {ROLE_MEMBER, 'ROLE_VIP'}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<OrderFormModel> getById(@PathVariable Long orderId){
        return orderService.findById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("GET ORDER FAILURE! Id="+orderId));
    }

    @DeleteMapping("/delete/{orderId}")
    @ApiOperation("刪除訂單by id: Delete Order By Id {ROLE_MEMBER, 'ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_VIP'}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_VIP')")
    public ResponseEntity<OrderFormModel> deleteById(@PathVariable Long orderId){
        return orderService.deleteById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("GET ORDER FAILURE! Id="+orderId));
    }

    @PatchMapping("/update/member/{orderId}")
    @ApiOperation("消費者取消訂單: Cancel Order {'ROLE_MEMBER', 'ROLE_VIP'}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VIP')")
    public ResponseEntity<OrderFormModel> updateById(@PathVariable Long orderId, Authentication auth){
        return orderService.updateById(orderId, auth)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("GET ORDER FAILURE! Id="+orderId));
    }

    @PatchMapping("/update/employee/{orderId}")
    @ApiOperation("員工更新訂單: Get All Orders {'ROLE_EMPLOYEE', 'ROLE_ADMIN'}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<OrderFormModel> updateById(@Valid @RequestBody OrderUpdateEmployee request, @PathVariable Long orderId){
        return orderService.updateById(request, orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("GET ORDER FAILURE! Id="+orderId));
    }

}
