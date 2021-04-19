package com.seminar.easyCookWeb.pojo.recipe;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recipe_id")
    private long id;

    @Column(columnDefinition = "nvarchar(256)")
    @Nationalized
    private String name;

    @Column(columnDefinition = "nvarchar(20)")
    @Builder.Default
    private String version = "NORMAL";

    @Column(columnDefinition="TEXT")
    private String description;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Builder.Default
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RecipeImage> photos = new LinkedList<>();

    @Column(columnDefinition = "nvarchar(254)")
    private String link;

    @Builder.Default
    private double price = 0D;

    @Builder.Default
    private int likesCount = 0;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<RecipeStep> recipeSteps = new LinkedList<>();

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<RecipeIngredient> recipeIngredients = new LinkedList<>();

}
