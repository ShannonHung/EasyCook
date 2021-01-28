package com.seminar.easyCookWeb.pojo.recipe;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(columnDefinition = "nvarchar(256)")
    @Nationalized
    private String name;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Column(length = 1000)
    private byte[] photo;

    @Column(columnDefinition = "nvarchar(254)")
    private String link;

    private int likesCount;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RecipeStep> recipeSteps;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RecipeIngredient> recipeIngredients;

}
