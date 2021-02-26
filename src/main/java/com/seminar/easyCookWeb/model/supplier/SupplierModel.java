package com.seminar.easyCookWeb.model.supplier;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.supplier.SupplierPerson;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Supplier請求Entity樣式")
public class SupplierModel {

    private long iid;

    @NotBlank
    private String companyName;


    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    private String taxIdNumber;

    private String address;

    private String bankName;

    private String bankId;

    private String bankAccount;

    @Builder.Default
    private List<SupplierPersonModel> supplierPeople = new LinkedList<>();

}
