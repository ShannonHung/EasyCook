package com.seminar.easyCookWeb.Model;

import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.ingredient.Category;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModelTest {
    @Test
    public void RecipeModelTest(){
//        Ingredient ingredient = Ingredient.builder()
//                .id(0L)
//                .name("大陸妹")
//                .category(Category.VAGETABLE)
//                .city("Taiwan")
//                .country("Taipei")
//                .kcal(52.38)
//                .price(89)
//                .stock(0D)
//                .satefyStock(0D)
//                .unit("把").build();
//
//        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
//                .ingredient(ingredient)
//                .id(0L)
//                .Quantityrequired(10D)
//                .build();
//
//        Recipe recipe1 = Recipe.builder()
//                .id(0L)
//                .link("http://youtube...")
//                .likesCount(10)
//                .name("東坡肉飯")
//                .photo(null)
//                .recipeIngredients();
//        Member member = Member.builder()
//                .id(1L)
//                .account("admin")
//                .password("123")
//                .username("hung").build();
    }
    @Test
    public void SupplierTest() {
        SupplierPerson person = new SupplierPerson();


        SupplierPerson person1 = SupplierPerson.builder()
                .name("person")
                .phone("09")
                .build();
    }

}
