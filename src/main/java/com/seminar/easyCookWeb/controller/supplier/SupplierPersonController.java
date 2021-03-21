package com.seminar.easyCookWeb.controller.supplier;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.model.supplier.SupplierPersonModel;
import com.seminar.easyCookWeb.service.supplier.SupplierPersonService;
import com.seminar.easyCookWeb.service.supplier.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * @param personModel 合作商聯絡人
     * @return personModel
     */
    @PostMapping("/create/{supplierId}")
    @ApiOperation("建立合作商聯絡人: Create SupplierPerson {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<SupplierPersonModel> create(@PathVariable Long supplierId, @Valid @RequestBody SupplierPersonModel personModel){
        log.error("[SupplierPerson create] => " + personModel);
        return supplierPersonService.createPerson(supplierId,personModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create SupplierPerson!"));
    }

    @GetMapping("/all")
    @ApiOperation("取得所有合作商聯絡人清單: Get All SupplierPerson. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<Iterable<SupplierPersonModel>> findAll(){
        return supplierPersonService.findAll()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("SupplierPerson list is empty!"));
    }

    @GetMapping("/{supplierpersonId}")
    @ApiOperation("透過ID搜尋合作商聯絡人: Search SupplierPerson By id. {EVERYONE CAN ACCESS}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<SupplierPersonModel> findById(@PathVariable Long supplierpersonId){
        return supplierPersonService.findById(supplierpersonId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find SupplierPerson which id is " + supplierpersonId));
    }

    @DeleteMapping(path = "/delete/{supplierpersonId}")
    @ApiOperation("刪除合作商聯絡人: DELETE SupplierPerson BY id. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SupplierModel.class))),
            @ApiResponse(responseCode = "400", description = "Cannot find the supplierPerson")
    })
    public ResponseEntity<SupplierPersonModel> deleteById(@PathVariable Long supplierpersonId) {
        return supplierPersonService.delete(supplierpersonId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete supplierPerson fail"));
    }

    @PatchMapping("/update/{supplierpersonId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("透過id來更新合作商聯絡人: Update SupplierPerson By Id. (Role: ROLE_ADMIN, ROLE_EMPLOYEE)")
    public ResponseEntity<SupplierPersonModel> updateById(@PathVariable Long supplierpersonId, @Valid @RequestBody SupplierPersonModel request){
        return supplierPersonService.update(supplierpersonId, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update supplierPerson"));
    }
}
