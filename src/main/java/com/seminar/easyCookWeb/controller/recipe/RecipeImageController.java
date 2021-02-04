package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeImageMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import com.seminar.easyCookWeb.repository.recipe.RecipeImageRepository;
import com.seminar.easyCookWeb.service.recipe.RecipeImageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@Slf4j
@RequestMapping(value = "/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeImageController {
    @Autowired
    RecipeImageRepository imageRepository;
    @Autowired
    RecipeImageService imageService;
    @Autowired
    RecipeImageMapper mapper;

    @SneakyThrows
    @PostMapping("/image/upload/{recipeId}")
    public ResponseEntity<RecipeImageModel> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long recipeId){
        return imageService.saveImage(file, recipeId)
                .map(model -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/files/")
                            .path(model.getId().toString())
                            .toUriString();

                    model.setUrl(fileDownloadUri);
                    return  model;
                })
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Upload File Error"));
    }

    @GetMapping("/images/download/{recipeId}")
    public ResponseEntity<List<RecipeImageModel>> getListFiles(@PathVariable Long recipeId){
        return Optional.of(imageService.getFileByRecipeId(recipeId))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find all images"));
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
        return imageService.getFile(id)
                .map(file -> file.getPicByte())
                .map(ResponseEntity::ok)
                .orElseThrow(()->new BusinessException("Get file " + id + "Failure"));
    }

}
