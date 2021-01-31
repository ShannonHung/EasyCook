package com.seminar.easyCookWeb.model.ingredient;

import com.seminar.easyCookWeb.pojo.ingredient.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class IngredientModel {
    private long id;

    private String name;

    private Category category = Category.OTHER;

    private String unit;

    private Double kcal;

    private String country;

    private String city;

    @Builder.Default
    private int price = 0;
    @Builder.Default
    private Double satefyStock = 0D;
    @Builder.Default
    private Double stock = 0D;


}
