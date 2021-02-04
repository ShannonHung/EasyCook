package com.seminar.easyCookWeb.RecipeTest;

import com.seminar.easyCookWeb.EasyCookWebApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@SpringBootTest(classes = EasyCookWebApplication.class)
public class RecipeTest {
    @Test
    public void testIngredient(){

    }
}
