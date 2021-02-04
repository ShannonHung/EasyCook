package com.seminar.easyCookWeb.model.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RecipeImageModel {
    private Long id;
    private String name;
    private String url;
    private String type;
    private Long size;
}
