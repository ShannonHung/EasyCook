package com.seminar.easyCookWeb.service.recipe;

import com.seminar.easyCookWeb.mapper.recipe.RecipeImageMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeImageModel;
import com.seminar.easyCookWeb.pojo.recipe.RecipeImage;
import com.seminar.easyCookWeb.repository.recipe.RecipeImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class RecipeImageService {
    @Autowired
    RecipeImageRepository imageRepository;
    @Autowired
    RecipeImageMapper mapper;


    public Optional<RecipeImageModel> saveImage(MultipartFile file) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("[show StringUtils.cleanPath and file.getOriginalFile] => " + fileName + "\n" + file.getOriginalFilename());
        RecipeImage image = new RecipeImage(fileName, file.getContentType(), file.getBytes());

        return Optional.ofNullable(imageRepository.save(image))
                .map(pojo -> {
                    RecipeImageModel model = mapper.toModel(pojo);
                    model.setSize(file.getSize());
                    return model;
                });
    }
    public Optional<RecipeImage> getFile(Long id){
        return imageRepository.findById(id);
    }
    public Stream<RecipeImage> getAllFiles(){
        return imageRepository.findAll().stream();
    }
}
