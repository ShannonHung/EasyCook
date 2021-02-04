package com.seminar.easyCookWeb.controller.Ingredient;

import com.seminar.easyCookWeb.controller.user.EmployeeController;
import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.ingredient.IngredientName;
import com.seminar.easyCookWeb.service.ingredient.IngredientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/ingredient" , produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "食材Ingredient連接口", description = "提供食材相關的 Rest API")
public class IngredientController {
    @Autowired
    IngredientService service;

    @PostMapping("/create")
    @ApiOperation("建立食材: Create Ingredient {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<IngredientModel> createIngredient(@Valid @RequestBody IngredientModel request){
        return service.create(request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create Ingredient"));
    }

    @GetMapping("/id/{ingredientId}")
    @ApiOperation("透過ID搜尋食材: Search Ingredient By id {EVERYONE CAN ACCESS}")
    public ResponseEntity<IngredientModel> findById(@PathVariable Long ingredientId){
        return service.readByIngredientId(ingredientId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find ingredient which id is " + ingredientId));
    }

    @PostMapping("/name")
    @ApiOperation("透過食材名稱模糊搜尋食材: Search Ingredient By NAME {EVERYONE CAN ACCESS}")
    public ResponseEntity<Iterable<IngredientModel>> findByName(@RequestBody IngredientName name){
        return service.readByIngredientName(name.getIngredientName())
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find ingredient which name is " + name.getIngredientName()));
    }

    @GetMapping("/all")
    @ApiOperation("取得食材清單: GET ALL INGREDIENTS {EVERYONE CAN ACCESS}")
    public ResponseEntity<Iterable<IngredientModel>> getAll(){
        return service.getAll()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Ingredient list is empty!"));
    }

    @ApiOperation("刪除食材清單: DELETE INGREDIENT BY ID {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = IngredientModel.class))),
            @ApiResponse(responseCode = "400", description = "Cannot find the account")
    })
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{ingredientId}")
    public ResponseEntity<IngredientModel> deleteById(@PathVariable Long ingredientId) {
        return service.delete(ingredientId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete account fail"));
    }

}
