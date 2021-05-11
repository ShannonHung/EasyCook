package com.seminar.easyCookWeb.pojo.recipe;

import com.seminar.easyCookWeb.pojo.appUser.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FavoriteRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="favorite_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private Recipe recipe;
}
