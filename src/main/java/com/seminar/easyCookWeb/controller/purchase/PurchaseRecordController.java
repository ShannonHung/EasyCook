package com.seminar.easyCookWeb.controller.purchase;


import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.purchase.PurchaseRecordModel;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.service.purchase.PurchaseRecordService;
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
import java.util.List;

@RestController
@RequestMapping(value = "/purchase/record" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "進貨紀錄PurchaseRecord連接口", description = "提供進貨紀錄相關的 Rest API")
public class PurchaseRecordController {
    @Autowired
    private PurchaseRecordService purchaseRecordService;

    /**
     * 建立進貨紀錄
     * @param purchaseRecordModel 進貨紀錄
     * @return purchaseRecordModel
     */
    @PostMapping("/create/{supplierId}")
    @ApiOperation("建立進貨紀錄: Create PurchaseRecord {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<PurchaseRecordModel> create(@PathVariable Long supplierId, @Valid @RequestBody PurchaseRecordModel purchaseRecordModel){
        log.error("[SupplierPerson create] => " + purchaseRecordModel);
        return purchaseRecordService.create(supplierId,purchaseRecordModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create PurchaseRecord!"));
    }

    @GetMapping("/all")
    @ApiOperation("取得所有進貨紀錄清單: Get All SupplierPerson. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<Iterable<PurchaseRecordModel>> findAll(){
        return purchaseRecordService.findAll()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("PurchaseRecord list is empty!"));
    }

    @GetMapping("/{recordId}")
    @ApiOperation("透過PurchaseRecordID搜尋進貨紀錄: Search SupplierPerson By id. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<PurchaseRecordModel> findById(@PathVariable Long recordId){
        return purchaseRecordService.findById(recordId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find PurchaseRecord which id is " + recordId));
    }

    @GetMapping("/all/{supplierId}")
    @ApiOperation("透過SupplierID搜尋進貨紀錄: Search PurchaseRecord By id. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<List<PurchaseRecordModel>> findBySupplierId(@PathVariable Long supplierId){
        return purchaseRecordService.findBySupplierId(supplierId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find PurchaseRecord which Supplier Id is " + supplierId));
    }

    @DeleteMapping(path = "/delete/{recordId}")
    @ApiOperation("刪除進貨紀錄:: DELETE SupplierPerson BY id. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SupplierModel.class))),
            @ApiResponse(responseCode = "400", description = "Cannot find the PurchaseRecord")
    })
    public ResponseEntity<PurchaseRecordModel> deleteById(@PathVariable Long recordId) {
        return purchaseRecordService.delete(recordId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete supplierPerson fail"));
    }

    @PatchMapping("/update/{recordId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("透過id來更新進貨紀錄:: Update SupplierPerson By Id. (Role: ROLE_ADMIN, ROLE_EMPLOYEE)")
    public ResponseEntity<PurchaseRecordModel> updateById(@PathVariable Long recordId, @Valid @RequestBody PurchaseRecordModel request){
        return purchaseRecordService.update(recordId, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update PurchaseRecord"));
    }

}
