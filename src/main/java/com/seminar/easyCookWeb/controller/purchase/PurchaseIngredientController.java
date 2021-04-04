package com.seminar.easyCookWeb.controller.purchase;


import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.purchase.PurchaseIngredientModel;
import com.seminar.easyCookWeb.model.purchase.PurchaseRecordModel;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
import com.seminar.easyCookWeb.service.purchase.PurchaseIngredientService;
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
@RequestMapping(value = "/purchase/ingredient" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "進貨-食材 PurchaseRecord連接口", description = "提供進貨-食材相關的 Rest API")
public class PurchaseIngredientController {
    @Autowired
    private PurchaseIngredientService purchaseIngredientService;

    /**
     * 建立進貨紀錄
     * @param purchaseIngredientModel 進貨紀錄
     * @return PurchaseIngredientModel
     */
    @PostMapping("/create/{purchaseRecordId}/{ingredientId}")
    @ApiOperation("建立進貨紀錄: Create PurchaseRecord {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<PurchaseIngredientModel> create(@PathVariable Long purchaseRecordId,@PathVariable Long ingredientId, @Valid @RequestBody PurchaseIngredientModel purchaseIngredientModel){
        log.error("[SupplierPerson create] => " + purchaseIngredientModel);
        return purchaseIngredientService.create(purchaseRecordId,ingredientId,purchaseIngredientModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create PurchaseIngredient!"));
    }

    @GetMapping("/all")
    @ApiOperation("取得所有進貨-食材清單: Get All PurchaseIngredient. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<Iterable<PurchaseIngredientModel>> findAll(){
        return purchaseIngredientService.findAll()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("PurchaseIngredient list is empty!"));
    }

    @GetMapping("/{purchaseIngredientId}")
    @ApiOperation("透過PurchaseIngredientID搜尋一筆進貨-食材: Search PurchaseIngredient By id. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<PurchaseIngredientModel> findById(@PathVariable Long purchaseIngredientId){
        return purchaseIngredientService.findById(purchaseIngredientId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find PurchaseIngredient which id is " + purchaseIngredientId));
    }

    @GetMapping("/all/{recordId}")
    @ApiOperation("透過PurchaseRecordId搜尋進貨-食材所有紀錄: Search PurchaseIngredient By PurchaseRecord id. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<List<PurchaseIngredientModel>> findByRecordId(@PathVariable Long recordId){
        return purchaseIngredientService.findByRecordId(recordId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find PurchaseIngredient which PerchaseRecord Id is " + recordId));
    }

    @DeleteMapping(path = "/delete/{purchaseIngredientId}")
    @ApiOperation("透過id來刪除該筆進貨-食材紀錄:: DELETE PurchaseIngredient BY id. {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SupplierModel.class))),
            @ApiResponse(responseCode = "400", description = "Cannot find the PurchaseRecord")
    })
    public ResponseEntity<PurchaseIngredientModel> deleteById(@PathVariable Long purchaseIngredientId) {
        return purchaseIngredientService.delete(purchaseIngredientId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete PurchaseIngredient fail"));
    }

    @PatchMapping("/update/{purchaseIngredientId}/{purchaseRecordId}/{ingredientId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("透過id來更新該筆進貨-食材紀錄:: Update PurchaseIngredient By Id. (Role: ROLE_ADMIN, ROLE_EMPLOYEE)")
    public ResponseEntity<PurchaseIngredientModel> updateById(@PathVariable Long purchaseIngredientId,@PathVariable Long purchaseRecordId,@PathVariable Long ingredientId, @Valid @RequestBody PurchaseIngredientModel request){
        return purchaseIngredientService.update(purchaseIngredientId,purchaseRecordId,ingredientId, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update PurchaseIngredient"));
    }
}
