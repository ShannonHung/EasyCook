package com.seminar.easyCookWeb.pojo.cart;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CartRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id")
    private Recipe recipe;

    @OneToMany(mappedBy = "cartRecipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<CartRecipeCustomize> customize = new LinkedList<>();
}
