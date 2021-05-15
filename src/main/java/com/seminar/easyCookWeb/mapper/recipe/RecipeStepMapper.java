package com.seminar.easyCookWeb.mapper.recipe;

import com.seminar.easyCookWeb.mapper.user.EmployeeMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.RecipeStepModel;
import com.seminar.easyCookWeb.model.recipe.update.RecipeUpdateModel;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.model.user.MemberRequest;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeStep;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.persistence.ManyToOne;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeStepMapper {
    RecipeStepMapper MAPPER = Mappers.getMapper( RecipeStepMapper.class );

    RecipeStepModel toModel(RecipeStep recipeStep);

    RecipeStep toPOJO(RecipeStepModel recipeStepModel);

    @Mapping(target = "id",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(RecipeStepModel recipeModel, @MappingTarget RecipeStep recipe);

    List<RecipeStepModel> toModels(List<RecipeStep> recipeStep);


}
