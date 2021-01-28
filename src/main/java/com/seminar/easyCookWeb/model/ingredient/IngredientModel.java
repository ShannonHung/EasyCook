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

    @NotBlank
    private String name;

    private Category category = Category.OTHER;

    private String unit;

    private Double kcal;

    private String country;

    private String city;

    private int price = 0;

    private Double satefyStock = 0D;

    private Double stock = 0D;


}
