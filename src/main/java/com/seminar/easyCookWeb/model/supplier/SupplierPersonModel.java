package com.seminar.easyCookWeb.model.supplier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Data
@ApiModel("合作商聯絡人SupplierPersonModel請求Entity樣式")
public class SupplierPersonModel {
    @ApiModelProperty(value = "合作商ID", required = true, example = "supplier001")
    private long iid;

    @ApiModelProperty(value = "合作商姓名", required = true, example = "大雄")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "合作商聯絡人職位", required = true, example = "大雄")
    private String position;

    @ApiModelProperty(value = "合作商姓名", required = true, example = "大雄")
    private String phone;

    @ApiModelProperty(value = "合作商姓名", required = true, example = "大雄")
    private String ext;

    @ApiModelProperty(value = "合作商姓名", required = true, example = "大雄")
    private String fax;

    @ApiModelProperty(value = "合作商姓名", required = true, example = "大雄")
    @NotBlank
    private String email;
}
