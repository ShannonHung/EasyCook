package com.seminar.easyCookWeb.pojo.supplier;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="supplier_id")
    private long iid;

    @Column(columnDefinition = "nvarchar(256)", name = "supplier_name")
    @Nationalized
    @NotBlank
    private String companyName;

    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime startDate;

    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime endDate;

    @Column(columnDefinition = "nvarchar(20)")
    private String taxIdNumber;

    @Column(columnDefinition = "nvarchar(256)", name = "supplier_address")
    private String address;

    @Column(columnDefinition = "nvarchar(50)", name = "bank_name")
    private String bankName;

    @Column(columnDefinition = "nvarchar(50)", name = "bank_id")
    private String bankId;

    @Column(columnDefinition = "nvarchar(256)", name = "bank_account")
    private String bankAccount;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<SupplierPerson> supplierPeople = new LinkedList<>();

}
