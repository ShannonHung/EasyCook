package com.seminar.easyCookWeb.pojo.ingredient;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ingredient_id")
    private long id;

    @Column(columnDefinition = "nvarchar(256)")
    @Nationalized
    @NotBlank
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "nvarchar(30)")
    private Category category;

    @Column(columnDefinition = "nvarchar(20)")
    private String unit;

    @Column(columnDefinition = "decimal(28,4)")
    private Double kcal;

    @Column(columnDefinition = "nvarchar(60)")
    private String country;

    @Column(columnDefinition = "nvarchar(60)")
    private String city;

    private int price;

    @Column(columnDefinition = "decimal(28,4)")
    @Builder.Default
    private Double safetyStock = 0D;

    @Column(columnDefinition = "decimal(28,4)")
    @Builder.Default
    private Double stock= 0D;


}
