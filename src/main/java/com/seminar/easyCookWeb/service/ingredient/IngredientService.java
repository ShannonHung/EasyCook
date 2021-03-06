package com.seminar.easyCookWeb.service.ingredient;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.ingredient.IngredientMapper;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.repository.ingredient.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientService {

    @Autowired
    IngredientRepository repository;
    @Autowired
    IngredientMapper mapper;

    /**
     * 新增食材
     * @param ingredientModel - 食材 DTO 物件
     * @return 新增厚的食材DTO 物件
     */
    public Optional<IngredientModel> create(IngredientModel ingredientModel){
        return Optional.ofNullable(Optional.ofNullable(mapper.toPOJO(ingredientModel))
                .filter(it -> repository.findByName(it.getName()).isEmpty())
                .map(repository::save)
                .map(mapper::toModel)
                .orElseThrow(() -> new EntityCreatedConflictException("An ingredient with same name have already existed!")));
    }

    /**
     * 取得所有食材
     * @return 從資料庫取得的食材DTO 物件清單
     */
    public Optional<Iterable<IngredientModel>> getAll(){
        return Optional.of(repository.findAll())
                .map(mapper::toIterableModel);
    }

    /**
     * 透過id尋找食材
     * @param ingredientId
     * @return
     */
    public Optional<IngredientModel> readByIngredientId(Long ingredientId){
        return repository.findById(ingredientId)
                .map(mapper::toModel);
    }

    /**
     * 透過名字尋找食材
     * @param name
     * @return
     */
    public Optional<Iterable<IngredientModel>> readByIngredientName(String name){
        return repository.findByName(name)
                .map(mapper::toIterableModel);
    }

    /**
     * 透過id刪除食材
     * @param id 食材id
     * @return
     */
    public Optional<IngredientModel> delete(Long id){
        System.out.println("[delete ingredient]" + id);
        System.out.println("[delete ingredient] find ingredient=> "  + repository.findById(id));
        return repository.findById(id)
                .map(it ->{
                    try {
                        repository.deleteById(it.getId());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted " +it.getId()+ " ingredient, maybe this ingredient was used by recipe");
                    }
                })
                .map(mapper::toModel);
    }

    public Optional<IngredientModel> update(Long iid, IngredientModel ingredientModel) {
        return Optional.of(repository.findById(iid))
                .map(it -> {
                    Ingredient origin = it.orElseThrow(() -> new EntityNotFoundException("Cannot find Ingredient"));
                    if(repository.ExistName(ingredientModel.getName(), iid) > 0){
                        throw new EntityCreatedConflictException("this ingredient have already in used!");
                    }else{
                        mapper.update(ingredientModel, origin);
                        return origin;
                    }
                })
                .map(repository::save)
                .map(mapper::toModel);
    }


}
