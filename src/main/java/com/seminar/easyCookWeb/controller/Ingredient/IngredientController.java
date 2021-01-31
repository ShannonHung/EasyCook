package com.seminar.easyCookWeb.controller.Ingredient;

import com.seminar.easyCookWeb.controller.user.EmployeeController;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.ingredient.IngredientName;
import com.seminar.easyCookWeb.service.ingredient.IngredientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/ingredient" , produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "食材Ingredient連接口", description = "提供食材相關的 Rest API")
public class IngredientController {
    @Autowired
    IngredientService service;

    @PostMapping("/create")
    @ApiOperation("建立食材: Create Ingredient")
    public ResponseEntity<IngredientModel> createIngredient(@Valid @RequestBody IngredientModel request){
        return service.create(request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create Ingredient"));
    }

    @GetMapping("/id/{ingredientId}")
    public ResponseEntity<IngredientModel> findById(@PathVariable Long ingredientId){
        return service.readByIngredientId(ingredientId)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find ingredient which id is " + ingredientId));
    }

    @PostMapping("/name")
    public ResponseEntity<Iterable<IngredientModel>> findByName(@RequestBody IngredientName name){
        return service.readByIngredientName(name.getIngredientName())
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find ingredient which name is " + name.getIngredientName()));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<IngredientModel>> getAll(){
        return service.getAll()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Ingredient list is empty!"));
    }

}
