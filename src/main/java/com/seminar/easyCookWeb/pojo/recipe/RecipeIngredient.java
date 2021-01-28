package com.seminar.easyCookWeb.pojo.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.List;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null
    @Column(name="recipe_ingredient_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    @JsonBackReference
    private Recipe recipe;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ingredient_id")
    @JsonManagedReference
    private Ingredient ingredient;

    @Column(columnDefinition = "decimal(28,4)")
    private Double Quantityrequired;

}
