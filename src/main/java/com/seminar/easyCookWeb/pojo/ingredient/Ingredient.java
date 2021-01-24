package com.seminar.easyCookWeb.pojo.ingredient;

import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeIngredient;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.Null;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null
    @Column(name="ingredient_id")
    private long id;

    @Column(length = 65)
    @Nationalized
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 40)
    private Category category;

    @Column(length = 40)
    private String unit;

    @Column(columnDefinition = "decimal(28,4)")
    private Double kcal;

    @Column(length = 65)
    private String country;

    @Column(length = 65)
    private String city;

    private int price;

    @Column(columnDefinition = "decimal(28,4)")
    private Double satefyStock;

    @Column(columnDefinition = "decimal(28,4)")
    private Double stock;


}
