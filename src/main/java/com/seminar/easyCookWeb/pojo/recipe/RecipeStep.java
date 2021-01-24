package com.seminar.easyCookWeb.pojo.recipe;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Null;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null
    @Column(name="recipe_step_id")
    private long id;

    @Column(length = 65)
    private String startTime;

    @Column(length = 2000)
    private String note;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}
