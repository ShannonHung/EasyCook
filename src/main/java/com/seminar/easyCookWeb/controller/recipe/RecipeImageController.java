package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeImageMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import com.seminar.easyCookWeb.repository.recipe.RecipeImageRepository;
import com.seminar.easyCookWeb.service.recipe.RecipeImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(tags = "食譜照片上傳與下載連接口", description = "提供上傳與下載食譜相片的 Rest API")
public class RecipeImageController {
    @Autowired
    RecipeImageRepository imageRepository;
    @Autowired
    RecipeImageService imageService;
    @Autowired
    RecipeImageMapper mapper;

    @SneakyThrows
    @PostMapping("/image/upload/{recipeId}")
    @ApiOperation("上傳食譜相片: Upload Photo {ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
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
    @ApiOperation("透過食譜Id取得食譜相片下載點: Download Photo By Recipe Id {EVERYONE CAN ACCESS}")
    public ResponseEntity<List<RecipeImageModel>> getListFiles(@PathVariable Long recipeId){
        return Optional.of(imageService.getFileByRecipeId(recipeId))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find all images"));
    }

    @GetMapping("/images/{id}")
    @ApiOperation("透過相片Id下載食譜相片: Download Photo By Photo Id {EVERYONE CAN ACCESS}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
        return imageService.getFile(id)
                .map(file -> file.getPicByte())
                .map(ResponseEntity::ok)
                .orElseThrow(()->new BusinessException("Get file " + id + "Failure"));
    }

}
