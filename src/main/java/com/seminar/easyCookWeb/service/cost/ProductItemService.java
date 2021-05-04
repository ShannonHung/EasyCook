package com.seminar.easyCookWeb.service.cost;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.cost.ProductItemMapper;
import com.seminar.easyCookWeb.model.cost.ProductItemModel;
import com.seminar.easyCookWeb.pojo.cost.HandmadeCost;
import com.seminar.easyCookWeb.pojo.cost.ProductItem;
import com.seminar.easyCookWeb.repository.cost.HandmadeRepository;
import com.seminar.easyCookWeb.repository.cost.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductItemService {
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private HandmadeRepository handmadeRepository;


    @Autowired
    private ProductItemMapper productItemMapper;

    /**
     * 將productItem裡面針對handmadeCost建立關聯
     * @param productItem 要塞的對象
     * @param handmadeCost 塞入的對象
     * @return 塞好的pojo
     */
    public Optional<ProductItem> save(ProductItemModel productItem, HandmadeCost handmadeCost){

        return Optional.of(productItem)
                .map(productItemMapper::toPojo)
                .map((pojo) -> pojo.toBuilder().handmadeCost(handmadeCost).build())
                .map(productItemRepository::save);
    }

    /**
     * 針對model lists 轉成 已經塞好資料的 pojo lists
     * @param productItemModels
     * @param handmadeId
     * @return
     */
    public List<ProductItem> saveList(List<ProductItemModel> productItemModels, long handmadeId){
        HandmadeCost handmadeCost = handmadeRepository.findById(handmadeId).orElseThrow(()-> new EntityNotFoundException("CANNOT FIND THE COST!"));

        return productItemModels.stream().map((item) -> save(item, handmadeCost).get())
                .collect(Collectors.toList());
    }

    /**
     * 針對更新
     * @param productItem
     * @param handmadeCost
     * @return
     */
    public Optional<ProductItem> update(ProductItem productItem, HandmadeCost handmadeCost){

        return Optional.of(productItem)
                .map((pojo) -> pojo.toBuilder().handmadeCost(handmadeCost).build())
                .map(productItemRepository::save);
    }

    /**
     * 更新裡面的每個item
     * @param productItems
     * @param handmadeId
     * @return
     */
    public List<ProductItem> updateList(List<ProductItem> productItems, long handmadeId){
        HandmadeCost handmadeCost = handmadeRepository.findById(handmadeId).orElseThrow(()-> new EntityNotFoundException("CANNOT FIND THE COST!"));

        return productItems.stream().map((item) -> update(item, handmadeCost).get())
                .collect(Collectors.toList());
    }
}
