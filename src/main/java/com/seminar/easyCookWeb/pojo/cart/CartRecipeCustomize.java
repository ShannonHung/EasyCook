package com.seminar.easyCookWeb.pojo.cart;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString(exclude = "shoppingCartRecipe")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CartRecipeCustomize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_recipe_customize_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    @JsonManagedReference
    private Ingredient ingredient;

    @Column(columnDefinition = "decimal(28,4)")
    @Builder.Default
    private Double quantityRequired = 0D;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private CartRecipe cartRecipe;
}
