package com.seminar.easyCookWeb.pojo.supplier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "supplier")
@Table(name = "supplier_contact_person")
public class SupplierPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="contact_id")
    private long iid;

    @Column(columnDefinition = "nvarchar(256)",name = "name")
    @Nationalized
    @NotBlank
    private String name;

    @Column(columnDefinition = "nvarchar(100)",name = "position")
    private String position;

    @Column(columnDefinition = "nvarchar(15)",name = "phone")
    @NotBlank
    private String phone;

    @Column(columnDefinition = "nvarchar(10)",name = "ext")
    private String ext;

    @Column(columnDefinition = "nvarchar(15)",name = "fax")
    private String fax;

    @Column(columnDefinition = "nvarchar(254)",name = "email")
    private String email;

    //設定"多對一"關聯後，記得另一邊也要設定"一對多"關聯
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @JsonBackReference
    private Supplier supplier;
}
