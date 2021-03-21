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
    public String FireBaseUrl(String parp){
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/recipe/images/firebase/")
                .path(parp)
                .toUriString();
    }

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
//        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

        //save image to mysql
        RecipeImage image = new RecipeImage(fileName, file.getContentType(), file.getBytes());
        RecipeModel recipemodel = recipeService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find recipe"));
        image.setRecipe(recipeMapper.toPOJO(recipemodel));

        //save image to firebase
        File multipartFile = this.convertToFile(file, fileName);                      // to convert multipartFile to File
        this.uploadFile(multipartFile, fileName);                                   // to get uploaded file link
        multipartFile.delete();
        String finalFileName = fileName;

        return Optional.ofNullable(imageRepository.save(image))
                .map(pojo -> {
                    RecipeImageModel model = imageMapper.toModel(pojo);
                    model.setSize(file.getSize());
                    model.setBlobUrl(BlobUrl(pojo.getId()));
                    model.setFirebaseUrl(FireBaseUrl(pojo.getName()));
                    return model;
                });
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

    /**
     * 透過照片id 取得firebase照片display連結
     *
     * @param imageName 經過uuid處理的filename
     * @return 含有14天期限firebase連結的model
     */
    public Optional<RecipeImageModel> getFirebaseUrlByName(String imageName) {
        String firebaseUrl = getDOWNLOAD_URL(imageName);
        return Optional.of(RecipeImageModel
                .builder()
                .firebaseUrl(firebaseUrl)
                .name(imageName)
                .build());
    }

    /**
     * 透過image id來找到photo 因為我發現如果使用imageRepository.findByName會回傳太多個結果但是都是相同的名稱
     *
     * @param recipeId 食譜id
     * @return firebaseUrl結果
     */
    public Optional<RecipeImageModel> getFirebaseUrlById(Long recipeId) {
        RecipeImageModel recipeImageModel = RecipeImageModel
                .builder()
                .firebaseUrl("NoUrl")
                .build();

        imageRepository.findById(recipeId)
                .ifPresent(pojo -> {
                    Optional.of(imageMapper.toModel(pojo))
                            .map((model -> {
                                String firebaseUrl = getDOWNLOAD_URL(model.getName());
                                recipeImageModel.setFirebaseUrl(firebaseUrl);
                                recipeImageModel.setName(model.getName());
                                recipeImageModel.setBlobUrl(BlobUrl(model.getId()));
                                recipeImageModel.setSize(model.getSize());
                                recipeImageModel.setId(model.getId());
//                                recipeImageModel
//                                        .toBuilder()
//                                        .firebaseUrl(firebaseUrl)
//                                        .name(model.getName())
//                                        .size(model.getSize())
//                                        .blobUrl(blobUrl)
//                                        .id(model.getId())
//                                        .build();
                                return recipeImageModel;
                            }));
                });
        return Optional.of(recipeImageModel);
    }

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
     *
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

    private String getDOWNLOAD_URL(String fileName) {

        try {
            BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
            Credentials credentials = null;
            credentials = GoogleCredentials.fromStream(new FileInputStream(ResourceUtils.getFile(PRIVATE_FIREBASE_KEY)));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            return String.valueOf(storage.signUrl(blobInfo, 14, TimeUnit.DAYS));

        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            return "Cannot find the photo in firebase";
        }

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
//            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

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
