package com.seminar.easyCookWeb.controller.supplier;

import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.model.supplier.SupplierPersonModel;
import com.seminar.easyCookWeb.service.supplier.SupplierPersonService;
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
@RequestMapping(value = "/supplierperson" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "合作商聯絡人SupplierPerson連接口", description = "提供合作商聯絡人相關的 Rest API")
public class SupplierPersonController {
    @Autowired
    private SupplierPersonService supplierPersonService;

    /**
     * 建立合作商聯絡人
     * @param personModel
     * @return
     */
//    //supplierperson/create到底要不要用呢 待討論
//    @PostMapping("/create")
//    @ApiOperation("建立合作商聯絡人: Create Supplier {ROLE_EMPLOYEE, ROLE_ADMIN}")
//    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
//    public ResponseEntity<SupplierPersonModel> create(@Valid @RequestBody Long supplierId, SupplierPersonModel personModel){
//        log.error("[recipe create] => " + personModel);
//        return supplierPersonService.createPerson(supplierId,personModel)
//                .map(ResponseEntity::ok)
//                .orElseThrow(()-> new EntitiesErrorException("Cannot create SupplierPerson!"));
//    }

}
