package com.seminar.easyCookWeb.pojo.cost;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.cart.CartRecipeCustomize;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandmadeCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="handmade_cost_id")
    private long id;

    @Column(length = 45, unique = true, nullable = false)
    @Builder.Default
    private double cost = 0;

    @Column(columnDefinition = "nvarchar(128)")
    private String name;

    @OneToMany(mappedBy = "handmadeCost", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<ProductItem> products = new LinkedList<>();
}
