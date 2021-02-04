package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.error.EntityErrorResponse;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.service.recipe.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/recipe" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping("/create")
    public ResponseEntity<RecipeModel> create(@RequestBody RecipeModel recipeModel){
        log.info("[recipe create] => " + recipeModel);
        return recipeService.createRecipe(recipeModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntitiesErrorException("Cannot create Recipe!"));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<RecipeModel>> findAll(){
        return recipeService.findAll()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cann not find any recipes"));
    }
}
