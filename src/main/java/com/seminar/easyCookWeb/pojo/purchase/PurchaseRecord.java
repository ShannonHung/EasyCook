package com.seminar.easyCookWeb.pojo.purchase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "supplier")
public class PurchaseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="purchase_id")
    private long iid;

//    @Column(columnDefinition = "nvarchar(256)",name = "ingredient_id")
//    @Nationalized
//    @NotBlank
//    private String ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @JsonBackReference
    private Supplier supplier;

    @Column(columnDefinition = "TIMESTAMP",name = "date")
    private OffsetDateTime date;

    @Column(columnDefinition = "decimal(28,4)",name = "discount")
    private Double discount = 0D ;

    @Column(columnDefinition = "decimal(28,4)",name = "total", nullable = false)
    @Builder.Default
    private Double total = 0D ;

}
