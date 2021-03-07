package com.seminar.easyCookWeb.config;

import com.seminar.easyCookWeb.mapper.user.EmployeeMapper;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Department;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.repository.users.EmployeeRepository;
import com.seminar.easyCookWeb.service.user.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DefaultDataConfig implements ApplicationRunner {

    private final Environment env;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private EmployeeMapper mapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //recipe
//        Recipe recipe = Recipe.builder()
//                .name("三明治")
//                .link("..//")
//                .likesCount(10).build();
//
//        RecipeStep step1 = RecipeStep.builder()
//                .recipe(recipe)
//                .note("切水果")
//                .startTime("0:51").build();
//
//        RecipeIngredient ingredient = RecipeIngredient.builder()
//                .ingredient(Ingredient.builder().name("ingredient").build())
//                .quantityRequired(10D)
//                .recipe(recipe)
//                .build();
//
//        List<RecipeStep> steps = new LinkedList<>();
//        List<RecipeIngredient> ingredients = new LinkedList<>();
//        ingredients.add(ingredient);
//        steps.add(step1);
//
//        recipe.setRecipeSteps(steps);
//        recipe.setRecipeIngredients(ingredients);
//
//        Recipe dbre = recipeRepository.save(recipe);

        //user
        EmployeeRequest admin = EmployeeRequest.builder()
                .account(env.getProperty("security.account.admin.username"))
                .password(env.getProperty("security.account.admin.password"))
                .username("admin hung")
                .role(Role.ADMIN)
                .department(Department.Sales)
                .email("admin@gmail.com")
                .phone("09787878")
                .title("Boss")
                .build();
        employeeRepository.findByAccount(admin.getAccount())
            .orElseGet(() -> {
                employeeService.saveEmployee(admin);
                return employeeRepository.findByAccount(admin.getAccount()).get();
            });
    }
}