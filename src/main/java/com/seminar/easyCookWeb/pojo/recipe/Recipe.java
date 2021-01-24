package com.seminar.easyCookWeb.pojo.recipe;

import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.util.List;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null
    @Column(name="recipe_id")
    private long id;

    @Column(length = 65)
    @Nationalized
    private String name;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Column(length = 1000)
    private byte[] photo;

    @Column(columnDefinition = "nvarchar(254)")
    private String link;

    private int likesCount;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<RecipeStep> recipeSteps;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<RecipeIngredient> recipeIngredients;

}
