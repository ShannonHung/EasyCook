package com.seminar.easyCookWeb.model.supplier;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Supplier請求Entity樣式")
public class SupplierModel {

    @ApiModelProperty(value = "合作商ID")
    private long iid;

    @NotNull
    @ApiModelProperty(value = "合作商名稱" ,example = "supplier001",required = true)
    private String companyName;

    @ApiModelProperty(value = "合作起始日" ,example = "2021-01-01T00:00+08:00")
    private OffsetDateTime startDate;

    @ApiModelProperty(value = "合作結束日" ,example = "2025-01-01T00:00+08:00")
    private OffsetDateTime endDate;

    @ApiModelProperty(value = "統一編號" ,example = "04126516")
    private String taxIdNumber;

    @ApiModelProperty(value = "公司地址" ,example = "台北市大安區基隆路三段43號")
    private String address;

    @ApiModelProperty(value = "銀行戶名" ,example = "台灣科技大學")
    private String bankName;

    @ApiModelProperty(value = "銀行代號" ,example = "000001")
    private String bankId;

    @ApiModelProperty(value = "銀行帳號" ,example = "1234567890")
    private String bankAccount;

    @Builder.Default
    private List<SupplierPersonModel> supplierPeople = new LinkedList<>();

}
