package com.seminar.easyCookWeb.service.recipe;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    private String DOWNLOAD_URL;
    private String TEMP_URL;
    private String PRIVATE_FIREBASE_KEY = "classpath:firebase/tsohue-backend-firebase-adminsdk-bcq36-309e8788f1.json";
    private String BUCKET_NAME = "tsohue-backend.appspot.com";



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

    /**
     * FireBase - UploadFile Function
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(ResourceUtils.getFile(PRIVATE_FIREBASE_KEY)));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8);
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public Object upload(MultipartFile multipartFile) {

        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            return ResponseEntity.ok()
                    .body("Successfully Uploaded !" + TEMP_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Unsuccessfully Uploaded!" + e);
        }

    }

    public Object download(String fileName) throws IOException {
        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));     // to set random strinh for destination file name
        String destFilePath = "C:\\ShannonFile\\" + destFileName;                                    // to set destination file path

        ////////////////////////////////   Download  ////////////////////////////////////////////////////////////////////////
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(ResourceUtils.getFile(PRIVATE_FIREBASE_KEY)));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of(BUCKET_NAME, fileName));
        blob.downloadTo(Paths.get(destFilePath));
        return ResponseEntity.ok()
                .body("Successfully Downloaded!");
    }

}
