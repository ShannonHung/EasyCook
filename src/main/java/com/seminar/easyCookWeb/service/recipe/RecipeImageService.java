package com.seminar.easyCookWeb.service.recipe;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.recipe.RecipeImageMapper;
import com.seminar.easyCookWeb.mapper.recipe.RecipeMapper;
import com.seminar.easyCookWeb.model.ingredient.IngredientModel;
import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.model.recipe.app.RecipeAppModel;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import com.seminar.easyCookWeb.repository.recipe.RecipeImageRepository;
import com.seminar.easyCookWeb.repository.recipe.RecipeRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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

    private String S3_URL_LINK = "https://easycook-backend.s3.amazonaws.com/recipe/";
//    private String BUCKET_NAME = "tsohue-backend.appspot.com";


    /**
     * 只存 mysql 裡面
     *
     * @param file
     * @param id
     * @return 會傳bloburl的連結 但是沒有firebaseUrl連結的照片model
     * @throws IOException
     */
    public Optional<RecipeImageModel> saveImage(MultipartFile file, Long id) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
        log.info("[show StringUtils.cleanPath and file.getOriginalFile] => " + fileName + "\n" + file.getOriginalFilename());
        RecipeImage image = new RecipeImage(fileName, file.getContentType(), file.getBytes());
        image.setRecipe(recipeService.findById(id).map(recipeMapper::toPOJO).get());

        return Optional.ofNullable(imageRepository.save(image))
                .map(pojo -> {
                    RecipeImageModel model = imageMapper.toModel(pojo);
                    model.setSize(file.getSize());
                    model.setS3Url(S3_URL_LINK+pojo.getName());
                    model.setBlobUrl(BlobUrl(model.getId()));
                    return model;
                });
    }

    public String BlobUrl(Long parp){
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/recipe/images/blob/")
                .path(parp.toString())
                .toUriString();
    }
//    public String FireBaseUrl(String parp){
//        return ServletUriComponentsBuilder
//                .fromCurrentContextPath()
//                .path("/recipe/images/firebase/")
//                .path(parp)
//                .toUriString();
//    }

    /**
     * 圖片同時存 資料庫 以及上傳至 firebase
     *
     * @param file
     * @param id
     * @return 含有照片id的下載點 model
     * @throws IOException
     */
    public Optional<RecipeImageModel> saveImageToFirebase(MultipartFile file, Long id) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //save image to mysql
        RecipeImage image = new RecipeImage(fileName, file.getContentType(), file.getBytes());
        RecipeModel recipemodel = recipeService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find recipe"));
        image.setRecipe(recipeMapper.toPOJO(recipemodel));

        return Optional.ofNullable(imageRepository.save(image))
                .map(pojo -> {
                    RecipeImageModel model = imageMapper.toModel(pojo);
                    model.setSize(file.getSize());
                    model.setBlobUrl(BlobUrl(pojo.getId()));
                    model.setS3Url(getS3PhotoUrl(pojo.getName()));
                    return model;
                });
    }

    public String getS3PhotoUrl(String filename){
        return URI.create(S3_URL_LINK+filename).toString();
    }

    /**
     * 透過照片名稱取得照片的blob
     *
     * @param imgname
     * @return
     */
    public Optional<RecipeImage> getFile(String imgname) {
        return imageRepository.findByName(imgname);
    }

    /**
     * 透過照片id取得blob
     *
     * @param id
     * @return
     */
    public Optional<RecipeImage> getFile(Long id) {
        return imageRepository.findById(id);
    }
//
//    /**
//     * 透過照片id 取得firebase照片display連結
//     *
//     * @param imageName 經過uuid處理的filename
//     * @return 含有14天期限firebase連結的model
//     */
//    public Optional<RecipeImageModel> getFirebaseUrlByName(String imageName) {
//        String firebaseUrl = getDOWNLOAD_URL(imageName);
//        return Optional.of(RecipeImageModel
//                .builder()
//                .firebaseUrl(firebaseUrl)
//                .name(imageName)
//                .build());
//    }
//
//    /**
//     * 透過image id來找到photo 因為我發現如果使用imageRepository.findByName會回傳太多個結果但是都是相同的名稱
//     *
//     * @param recipeId 食譜id
//     * @return firebaseUrl結果
//     */
//    public Optional<RecipeImageModel> getFirebaseUrlById(Long recipeId) {
//        RecipeImageModel recipeImageModel = RecipeImageModel
//                .builder()
//                .firebaseUrl("NoUrl")
//                .build();
//
//        imageRepository.findById(recipeId)
//                .ifPresent(pojo -> {
//                    Optional.of(imageMapper.toModel(pojo))
//                            .map((model -> {
//                                String firebaseUrl = getDOWNLOAD_URL(model.getName());
//                                recipeImageModel.setFirebaseUrl(firebaseUrl);
//                                recipeImageModel.setName(model.getName());
//                                recipeImageModel.setBlobUrl(BlobUrl(model.getId()));
//                                recipeImageModel.setSize(model.getSize());
//                                recipeImageModel.setId(model.getId());
////                                recipeImageModel
////                                        .toBuilder()
////                                        .firebaseUrl(firebaseUrl)
////                                        .name(model.getName())
////                                        .size(model.getSize())
////                                        .blobUrl(blobUrl)
////                                        .id(model.getId())
////                                        .build();
//                                return recipeImageModel;
//                            }));
//                });
//        return Optional.of(recipeImageModel);
//    }

    /**
     * 透過照片id 刪除照片
     *
     * @param id
     * @return 回傳沒有url的照片model
     */
    public Optional<RecipeImageModel> delete(Long id) {
        return imageRepository.findById(id)
                .map(it -> {
                    try {
                        imageRepository.deleteById(it.getId());
                        return it;
                    } catch (Exception ex) {
                        throw new BusinessException("Cannot Deleted ImagesId => " + it.getId());
                    }
                })
                .map(imageMapper::toModel);
    }

    /**
     * 會傳照片的連結
     *
     * @param recipeId
     * @return
     */
    public List<RecipeImageModel> getFileByRecipeId(Long recipeId) {
        return StreamSupport.stream(imageRepository.findByRecipeId(recipeId).spliterator(), false)
                .map(file -> {
                    Optional.of(file).orElseThrow(()-> new EntityNotFoundException("Cannot find this recipe which id is " + recipeId));
                    String blobUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/recipe/blob/images/")
                            .path(file.getId().toString())
                            .toUriString();

                    return RecipeImageModel.builder()
                            .id(file.getId())
                            .name(file.getName())
                            .blobUrl(blobUrl)
                            .s3Url(getS3PhotoUrl(file.getName()))
                            .type(file.getType())
                            .build();

                }).collect(Collectors.toList());
    }

    public Optional<String> getFirstImageByRecipeId(Long recipeId){

        return Optional.of(imageRepository.findByRecipeId(recipeId))
                .map(files -> {
                    if(files.isEmpty()) return "No Image";
                    RecipeImage file = files.stream().findFirst().get();
                    return getS3PhotoUrl(file.getName());
                });
    }

    public Optional<String> getFirstBlobImageIdByRecipeId(Long recipeId){
        return Optional.of(imageRepository.findByRecipeId(recipeId))
                .map(files -> {
                    if(files.isEmpty()) return "No Image";
                    return files.stream().findFirst().get().getId().toString();
                });
    }

}
