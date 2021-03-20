package com.seminar.easyCookWeb.model.recipe;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Rrecipe照片回應Entity樣式")
public class RecipeImageModel {
    private Long id;
    @ApiModelProperty(value = "相片名稱", example = "台灣風三明治.png")
    private String name;
    @ApiModelProperty(value = "blob照片連結", example = "http://140.118.9.145:8082/recipe/images/{id}")
    private String blobUrl;
    @ApiModelProperty(value = "firebase照片連結", example = "https://storage.googleapis.com/tsohue-backend.appspot.com/56651781-75a6-42da-a5ab-ede2d2318bb7.png?GoogleAccessId=firebase-adminsdk-bcq36@tsohue-backend.iam.gserviceaccount.com&Ex......")
    private String firebaseUrl;

    @ApiModelProperty(value = "檔案格式", example = "image/png")
    private String type;
    @ApiModelProperty(value = "檔案大小", example = "123456")
    private Long size;

}
