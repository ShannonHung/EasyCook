package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.ConflictException;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class IngredientService {
    @Autowired
    IngredientRepository ingredientRepository;


}
