package com.seminar.easyCookWeb.service.cost;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.cost.HandmadeMapper;
import com.seminar.easyCookWeb.model.cost.HandmadeModel;
import com.seminar.easyCookWeb.model.cost.HandmadeResponse;
import com.seminar.easyCookWeb.model.cost.ProductItemModel;
import com.seminar.easyCookWeb.pojo.cost.HandmadeCost;
import com.seminar.easyCookWeb.repository.cost.HandmadeRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import com.seminar.easyCookWeb.service.recipe.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HandmadeService {
    @Autowired
    private HandmadeRepository handmadeRepository;
    @Autowired
    private ProductItemService productItemService;
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private HandmadeMapper handmadeMapper;


    public Optional<HandmadeResponse> save(HandmadeModel cost){
        try{
            return Optional.of(cost)
                    .map(handmadeMapper::toPojo)
                    .map(handmadeRepository::save)
                    .map((pojo) -> {
                        pojo.setProducts(productItemService.saveList(cost.getProducts(), pojo.getId()));
                        HandmadeResponse response = handmadeMapper.toModel(pojo);
                        response.setProducts(
                                pojo.getProducts().stream()
                                        .map((porduct) -> recipeService.findById(porduct.getProductId()).get())
                                        .collect(Collectors.toList()));
                        return response;
                    });
        }catch (DataIntegrityViolationException e){
            throw new BusinessException("RECIPE HAVE ALREADY ASSIGNED THE COST!");
        }

    }

    public Optional<HandmadeCost> getInit(){
        return Optional.of(handmadeRepository.findInitCost().orElseThrow(()-> new EntityNotFoundException("CANNOT GET THE HANDMADE COST!")));
    }

}
