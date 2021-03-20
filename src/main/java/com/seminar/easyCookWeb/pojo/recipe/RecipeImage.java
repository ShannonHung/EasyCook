package com.seminar.easyCookWeb.pojo.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString(exclude = {"recipe"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeImage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="image_name", columnDefinition = "nvarchar(256)")
    private String name;

    @Column(name="image_type", columnDefinition = "nvarchar(256)")
    private String type;

    @Lob
    @Column(name="pic_byte")
    private byte[] picByte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @Nullable
    private Recipe recipe;

    public RecipeImage(String name, String type, byte[] data){
        this.name = name;
        this.type = type;
        this.picByte = data;
    }
}
