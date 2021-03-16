package com.seminar.easyCookWeb.controller.supplier;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.ingredient.IngredientName;
import com.seminar.easyCookWeb.model.supplier.SupplierModel;
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

    @GetMapping("/{supplierId}")
    @ApiOperation("透過ID搜尋合作商: Search Ingredient By id {EVERYONE CAN ACCESS}")
    public ResponseEntity<SupplierModel> findById(@PathVariable Long supplierId){
        return supplierService.findById(supplierId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find ingredient which id is " + supplierId));
    }

//    //Service的GETNAME做好才能用
//    @PostMapping("/name")
//    @ApiOperation("透過食材名稱模糊搜尋食材: Search Ingredient By NAME {EVERYONE CAN ACCESS}")
//    public ResponseEntity<Iterable<IngredientModel>> findByName(@RequestBody IngredientName name){
//        return service.readByIngredientName(name.getIngredientName())
//                .map(ResponseEntity::ok)
//                .orElseThrow(()-> new EntityNotFoundException("Cannot find ingredient which name is " + name.getIngredientName()));
//    }
//
//    @GetMapping("/all")
//    @ApiOperation("取得食材清單: GET ALL INGREDIENTS {EVERYONE CAN ACCESS}")
//    public ResponseEntity<Iterable<IngredientModel>> getAll(){
//        return service.getAll()
//                .map(ResponseEntity::ok)
//                .orElseThrow(()-> new EntityNotFoundException("Ingredient list is empty!"));
//    }

    @ApiOperation("刪除合作商名單: DELETE INGREDIENT BY ID {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SupplierModel.class))),
            @ApiResponse(responseCode = "400", description = "Cannot find the supplier")
    })
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{supplierId}")
    public ResponseEntity<SupplierModel> deleteById(@PathVariable Long supplierId) {
        return supplierService.delete(supplierId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete supplier fail"));
    }

    @PatchMapping("/update/{supplierId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("透過id來更新合作商: Update Supplier By Id (Role: ROLE_ADMIN, ROLE_EMPLOYEE)")
    public ResponseEntity<SupplierModel> updateById(@PathVariable Long supplierId, @Valid @RequestBody SupplierModel request){
        return supplierService.update(supplierId, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update supplier"));
    }
}
