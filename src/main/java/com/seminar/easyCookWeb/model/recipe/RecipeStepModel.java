package com.seminar.easyCookWeb.model.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Null;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStepModel {
    private long id;

    private String startTime;

    private String note;

}
