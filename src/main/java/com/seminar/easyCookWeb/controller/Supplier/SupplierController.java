package com.seminar.easyCookWeb.controller.Supplier;

import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.service.recipe.RecipeService;
import com.seminar.easyCookWeb.service.supplier.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/supplier" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "合作商Supplier連接口", description = "提供合作商相關的 Rest API")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    /**
     * 建立合作商
     * @param supplierModel supplierModel
     * @return
     */
    @PostMapping("/create")
    @ApiOperation("建立合作商: Create Supplier {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<SupplierModel> create(@Valid @RequestBody SupplierModel supplierModel){
        log.error("[recipe create] => " + supplierModel);
        return supplierService.createSupplier(supplierModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create Supplier!"));
    }
}
