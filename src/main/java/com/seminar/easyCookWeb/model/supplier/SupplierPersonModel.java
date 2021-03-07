package com.seminar.easyCookWeb.model.supplier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("合作商聯絡人SupplierPersonModel請求Entity樣式")
public class SupplierPersonModel {
    @ApiModelProperty(value = "合作商ID")
    private long iid;

    @ApiModelProperty(value = "姓名" , example = "葉大雄", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "職稱", example = "經理", required = true)
    private String position;

    @ApiModelProperty(value = "電話", example = "021234567", required = true)
    private String phone;

    @ApiModelProperty(value = "分機", example = "001")
    private String ext;

    @ApiModelProperty(value = "傳真", example = "123")
    private String fax;

    @ApiModelProperty(value = "電子郵件", example = "example@ex.com", required = true)
    private String email;
}
