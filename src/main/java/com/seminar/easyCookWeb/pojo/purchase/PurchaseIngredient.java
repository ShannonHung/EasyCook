package com.seminar.easyCookWeb.pojo.purchase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.supplier.Supplier;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"PurchaseRecord","ingredient"})
public class PurchaseIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="iid")
    private long iid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    @JsonBackReference
    private PurchaseRecord purchaseRecord;

//    @ManyToOne(fetch = FetchType.LAZY)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    @JsonBackReference
    private Ingredient ingredient;


    @Column(columnDefinition = "TIMESTAMP",name = "validate_date")
    private OffsetDateTime date;

    @Column(columnDefinition = "decimal(28,4)",name = "purchase_quantity", nullable = false)
    @Builder.Default
    private Double quantity = 0D ;

    @Column(columnDefinition = "decimal(28,4)",name = "purchase_price", nullable = false)
    @Builder.Default
    private Double price = 0D ;

    @Column(columnDefinition = "decimal(28,4)",name = "sum", nullable = false)
    @Builder.Default
    private Double sum = 0D ;



    


}
