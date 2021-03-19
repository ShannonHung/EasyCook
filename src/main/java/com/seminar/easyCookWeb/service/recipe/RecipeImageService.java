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

    private String DOWNLOAD_URL;
    private String TEMP_URL;
    private String PRIVATE_FIREBASE_KEY = "classpath:firebase/tsohue-backend-firebase-adminsdk-bcq36-309e8788f1.json";
    private String BUCKET_NAME = "tsohue-backend.appspot.com";


    /**
     * 只存 mysql 裡面 不會上傳至firebase
     * @param file
     * @param id
     * @return 會傳bloburl的連結 但是沒有firebaseUrl連結的照片model
     * @throws IOException
     */
    public Optional<RecipeImageModel> saveImage(MultipartFile file, Long id) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
        log.info("[show StringUtils.cleanPath and file.getOriginalFile] => " + fileName + "\n" + file.getOriginalFilename());
        RecipeImage image = new RecipeImage(fileName, file.getContentType(), file.getBytes());
        image.setRecipe(recipeService.findById(id).map(recipeMapper::toPOJO).get());

        return Optional.ofNullable(imageRepository.save(image))
                .map(pojo -> {
                    RecipeImageModel model = imageMapper.toModel(pojo);
                    model.setSize(file.getSize());
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/recipe/images/blob/")
                            .path(model.getId().toString())
                            .toUriString();

                    model.setBlobUrl(fileDownloadUri);
                    return model;
                });
    }

    /**
     * 圖片同時存 資料庫 以及上傳至 firebase
     * @param file
     * @param id
     * @return 含有照片id的下載點 model
     * @throws IOException
     */
    public Optional<RecipeImageModel> saveImageToFirebase(MultipartFile file, Long id) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

        //save image to mysql
        RecipeImage image = new RecipeImage(fileName, file.getContentType(), file.getBytes());
        image.setRecipe(recipeService.findById(id).map(recipeMapper::toPOJO).get());

        //save image to firebase
        File multipartFile = this.convertToFile(file, fileName);                      // to convert multipartFile to File
        this.uploadFile(multipartFile, fileName);                                   // to get uploaded file link
        multipartFile.delete();
        String finalFileName = fileName;

        return Optional.ofNullable(imageRepository.save(image))
                .map(pojo -> {
                    RecipeImageModel model = imageMapper.toModel(pojo);
                    model.setSize(file.getSize());
                    String blobUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/recipe/images/blob/")
                            .path(pojo.getId().toString())
                            .toUriString();

                    String firebaseUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/recipe/images/firebase/")
                            .path(pojo.getName())
                            .toUriString();

                    model.setBlobUrl(blobUrl);
                    model.setFirebaseUrl(firebaseUrl);
                    return model;
                });
    }

    /**
     * 透過照片id取得照片的blob
     * @param imgname
     * @return
     */
    public Optional<RecipeImage> getFile(String imgname){
        return imageRepository.findByName(imgname);
    }
    public Optional<RecipeImage> getFile(Long id){
        return imageRepository.findById(id);
    }

    /**
     * 透過照片id 取得firebase照片display連結
     * @param imageName 經過uuid處理的filename
     * @return 含有14天期限firebase連結的model
     */
    //TODO turn id into filename
    public Optional<RecipeImageModel> getFirebaseUrlById(String imageName){
        return imageRepository.findByName(imageName)
                .map(imageMapper::toModel)
                .map((model -> {
                    String FirebaseUrl = getDOWNLOAD_URL(model.getName(), model.getType());
                    model.setFirebaseUrl(FirebaseUrl);
                    return model;
                }));
    }

    /**
     * 透過照片id 刪除照片
     * @param id
     * @return 回傳沒有url的照片model
     */
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

    /**
     * 會傳照片的連結
     * @param recipeId
     * @return
     */
    public List<RecipeImageModel> getFileByRecipeId(Long recipeId){
        return StreamSupport.stream(imageRepository.findByRecipeId(recipeId).spliterator(), false)
                .map(file -> {
                    String blobUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/recipe/blob/images/")
                            .path(file.getId().toString())
                            .toUriString();

                    String firebaseUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/recipe/firebase/images/")
                            .path(file.getName())
                            .toUriString();

                    return RecipeImageModel.builder()
                            .id(file.getId())
                            .name(file.getName())
                            .blobUrl(blobUrl)
                            .firebaseUrl(firebaseUrl)
                            .size(Long.valueOf(file.getPicByte().length))
                            .type(file.getType())
                            .build();
                }).collect(Collectors.toList());
    }

    /**
     * FireBase - 上傳檔案至firebase功能
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    private void uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(ResourceUtils.getFile(PRIVATE_FIREBASE_KEY)));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
    }

    @SneakyThrows
    private String getDOWNLOAD_URL(String fileName, String contentType){
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(ResourceUtils.getFile(PRIVATE_FIREBASE_KEY)));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        return String.valueOf(storage.signUrl(blobInfo, 14, TimeUnit.DAYS));
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
            this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            log.info("Successfully Uploaded !");
            return "Successfully Uploaded !";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Unsuccessfully Uploaded!" + e);
        }
    }


}
