package com.seminar.easyCookWeb.pojo.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderItemCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_recipe_customize_id")
    private long id;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ingredientordered_id")
//    @JsonManagedReference
//    private Ingredient ingredient;

    @Column(columnDefinition = "nvarchar(256)")
    @Nationalized
    private String ingredientName;

    @Builder.Default
    @Column(columnDefinition = "decimal(28,4)")
    private double ingredientPrice = 0;

    @Column(columnDefinition = "decimal(28,4)")
    @Builder.Default
    private Double quantityRequired = 0D;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonBackReference
    private OrderItem orderItem;

}
