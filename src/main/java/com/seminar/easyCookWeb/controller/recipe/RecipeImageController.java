package com.seminar.easyCookWeb.controller.recipe;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeImageMapper;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import com.seminar.easyCookWeb.repository.recipe.RecipeImageRepository;
import com.seminar.easyCookWeb.service.recipe.RecipeImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
@RequestMapping(value = "/recipe/images", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "食譜照片上傳與下載連接口", description = "提供上傳與下載食譜相片的 Rest API")
public class RecipeImageController {
    @Autowired
    RecipeImageRepository imageRepository;
    @Autowired
    RecipeImageService imageService;
    @Autowired
    RecipeImageMapper mapper;

    @SneakyThrows
    @PostMapping("/upload/blob/{recipeId}")
    @ApiOperation("上傳食譜相片到mysql: Upload Photo to Database{ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<RecipeImageModel> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long recipeId){
        return imageService.saveImage(file, recipeId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Upload File Error"));
    }

    @SneakyThrows
    @PostMapping("/upload/firebase/{recipeId}")
    @ApiOperation("上傳食譜相片到firebase和mysql: Upload Photo to database and mysql{ROLE_EMPLOYEE, ROLE_ADMIN}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<RecipeImageModel> uploadFileToFirebase(@RequestParam("file") MultipartFile file, @PathVariable Long recipeId){
        return imageService.saveImageToFirebase(file, recipeId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Upload File Error"));
    }

    @GetMapping("/all/{recipeId}")
    @ApiOperation("透過食譜Id取得食譜相片下載點: Download Photo By Recipe Id {EVERYONE CAN ACCESS}")
    public ResponseEntity<List<RecipeImageModel>> getListFiles(@PathVariable Long recipeId){
        return Optional.of(imageService.getFileByRecipeId(recipeId))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find all images"));
    }

    @GetMapping("/blob/{imageId}")
    @ApiOperation("透過相片Id取得照片的blob: Download Photo By Photo Id {EVERYONE CAN ACCESS}")
    public ResponseEntity<byte[]> getFile(@PathVariable  Long imageId){
        return imageService.getFile(imageId)
                .map(it -> it.getPicByte())

                .map(ResponseEntity::ok)
                .orElseThrow(()->new BusinessException("Get file " + imageId + "Failure"));
    }

    @GetMapping("/firebase/{imageName}")
    @ApiOperation("透過相片Id取得firebase照片連結: Download Photo By Photo Id {EVERYONE CAN ACCESS}")
    public ResponseEntity<RecipeImageModel> getFileFromFirebase(@PathVariable String imageName){
        return imageService.getFirebaseUrlByName(imageName)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new BusinessException("Get file " + imageName + "Failure"));
    }


    @GetMapping("/downloadtolocal/{id}")
    @ApiOperation("透過相片Id下載食譜相片可以選擇要下載到哪裡: Download Photo By Photo Id {EVERYONE CAN ACCESS}")
    public ResponseEntity<byte[]> saveFileToLocal(@PathVariable Long id){
        return imageService.getFile(id)
                .map(file -> {
                    ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                            .filename(file.getName())
                            .build();
                    log.info("test =>" + contentDisposition.getFilename());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentDisposition(contentDisposition);
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                    return ResponseEntity.ok().contentLength(file.getPicByte().length)
                            .headers(headers)
                            .body(file.getPicByte());
                })
                .orElseThrow(()->new BusinessException("Get file " + id + "Failure"));

    }


    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{Id}")
    public ResponseEntity<RecipeImageModel> deleteById(@PathVariable Long Id) {
        return imageService.delete(Id)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new BusinessException("Cannot Deleted ImagesId => " + Id));
    }

//    @PostMapping("/profile/pic")
//    public Object upload(@RequestParam("file") MultipartFile multipartFile) {
//        log.info("HIT -/upload | File Name : {}", multipartFile.getOriginalFilename());
//        return imageService.upload(multipartFile);
//    }

//    @PostMapping("/profile/pic/{fileName}")
//    public Object download(@PathVariable String fileName) throws IOException {
//        log.info("HIT -/download | File Name : {}", fileName);
//        return imageService.download(fileName);
//    }

}
