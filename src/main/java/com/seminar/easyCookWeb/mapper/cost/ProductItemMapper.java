package com.seminar.easyCookWeb.mapper.cost;

import com.seminar.easyCookWeb.model.cost.ProductItemModel;
import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.pojo.cost.ProductItem;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductItemMapper {
    ProductItemModel toModel(ProductItem productItem);

    ProductItem toPojo(ProductItemModel productItemModel);

    List<ProductItem> toPojos(List<ProductItemModel> productItems);
}
