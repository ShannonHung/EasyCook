package com.seminar.easyCookWeb.controller.cost;

import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.cart.response.CartRecipeModel;
import com.seminar.easyCookWeb.model.cost.HandmadeModel;
import com.seminar.easyCookWeb.model.cost.HandmadeResponse;
import com.seminar.easyCookWeb.pojo.cost.HandmadeCost;
import com.seminar.easyCookWeb.service.cost.HandmadeService;
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
import java.util.Optional;

@RestController
@RequestMapping(value = "/cost/handmade" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "人工計價連接口", description = "提供人工價格相關的 Rest API")
public class CostController {

    @Autowired
    private HandmadeService handmadeService;

    @PatchMapping("/add")
    @ApiOperation("新增定價策略: Add new HandMade Price Setting('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<HandmadeResponse> updateCartById(@Valid @RequestBody HandmadeModel request){
        return handmadeService.save(request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("FAIL TO UPDATE THE HANDMADE COST!"));
    }
//
//    @PatchMapping("/update/{handmadeId}")
//    @ApiOperation("更新增定價策略: Update new HandMade Price Setting('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<HandmadeModel> updateCartById(@PathVariable("handmadeId") Long handmadeId, @Valid @RequestBody HandmadeModel request){
//        return handmadeService.update(handmadeId, request)
//                .map(ResponseEntity::ok)
//                .orElseThrow(()-> new EntitiesErrorException("FAIL TO UPDATE THE HANDMADE COST!"));
//    }

}
