package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeImageMapper;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import com.seminar.easyCookWeb.repository.recipe.RecipeImageRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class RecipeImageService {
    @Autowired
    RecipeImageRepository imageRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeImageMapper imageMapper;
    @Autowired
    RecipeMapper recipeMapper;


    public Optional<RecipeImageModel> saveImage(MultipartFile file, Long id) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("[show StringUtils.cleanPath and file.getOriginalFile] => " + fileName + "\n" + file.getOriginalFilename());
        RecipeImage image = new RecipeImage(fileName, file.getContentType(), file.getBytes());
        image.setRecipe(recipeService.findById(id).map(recipeMapper::toPOJO).get());

        return Optional.ofNullable(imageRepository.save(image))
                .map(pojo -> {
                    RecipeImageModel model = imageMapper.toModel(pojo);
                    model.setSize(file.getSize());
                    return model;
                });
    }
    public Optional<RecipeImage> getFile(Long id){
        return imageRepository.findById(id);
    }

    public Optional<RecipeImageModel> delete(Long id){
        return imageRepository.findById(id)
                .map(it ->{
                    try {
                        imageRepository.deleteById(it.getId());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted ImagesId => " +it.getId());
                    }
                })
                .map(imageMapper::toModel);
    }

    public List<RecipeImageModel> getFileByRecipeId(Long recipeId){
        return StreamSupport.stream(imageRepository.findByRecipeId(recipeId).spliterator(), false)
                .map(file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/recipe/images/")
                            .path(file.getId().toString())
                            .toUriString();

                    return RecipeImageModel.builder()
                            .id(file.getId())
                            .name(file.getName())
                            .url(fileDownloadUri)
                            .size(Long.valueOf(file.getPicByte().length))
                            .type(file.getType())
                            .build();
                }).collect(Collectors.toList());

//        return imageRepository.findByRecipeId(recipeId);
    }

    public Stream<RecipeImage> getAllFiles(){
        return imageRepository.findAll().stream();
    }
}
