package com.seminar.easyCookWeb.service.cost;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.cost.HandmadeMapper;
import com.seminar.easyCookWeb.model.cost.HandmadeModel;
import com.seminar.easyCookWeb.pojo.cost.HandmadeCost;
import com.seminar.easyCookWeb.repository.cost.HandmadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HandmadeService {
    @Autowired
    private HandmadeRepository handmadeRepository;
    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private HandmadeMapper handmadeMapper;


    public Optional<HandmadeModel> save(HandmadeModel cost){
        return Optional.of(cost)
                .map(handmadeMapper::toPojo)
                .map(handmadeRepository::save)
                .map((pojo) -> {
                    pojo.setProducts(productItemService.saveList(cost.getProducts(), pojo.getId()));
                    return pojo;
                })
                .map(handmadeMapper::toModel);
    }

    public Optional<HandmadeModel> update(Long handmadeId, HandmadeModel request){
        HandmadeCost current = handmadeRepository.findById(handmadeId).orElseThrow(()-> new EntityNotFoundException("CANNOT FIND THE HANDMADE COST!"));
        handmadeMapper.update(request, current);
        return Optional.of(current)
                .map(handmadeRepository::save)
                .map((pojo) -> {
                    pojo.setProducts(productItemService.updateList(current.getProducts(), pojo.getId()));
                    return pojo;
                })
                .map(handmadeMapper::toModel);
    }
}
