package com.seminar.easyCookWeb.pojo.cost;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private long productId;

    @NotBlank
    @Column(columnDefinition = "nvarchar(64)")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handmade_cost_id", referencedColumnName = "handmade_cost_id")
    private HandmadeCost handmadeCost;
}
