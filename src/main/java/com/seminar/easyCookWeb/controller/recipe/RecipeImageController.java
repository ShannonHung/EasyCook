package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    @PostMapping("/upload")
    public ResponseEntity<RecipeImageModel> uploadFile(@RequestParam("file") MultipartFile file){
        return imageService.saveImage(file)
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

    @GetMapping("/files")
    public ResponseEntity<List<RecipeImageModel>> getListFiles(){
        List<RecipeImageModel> files = imageService.getAllFiles()
                .map(file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/files/")
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

        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
        return imageService.getFile(id)
                .map(file -> file.getPicByte())
                .map(ResponseEntity::ok)
                .orElseThrow(()->new BusinessException("Get file " + id + "Failure"));
    }

}
