package com.seminar.easyCookWeb.repository.recipe;

import com.seminar.easyCookWeb.pojo.ingredient.Ingredient;
import com.seminar.easyCookWeb.pojo.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    /**
     * 尋找相同名稱的食譜 為了找到不同版本的食譜
     * @param name 食譜名稱
     * @return 很多食譜
     */
    Optional<List<Recipe>> findByName(String name);

    /**
     * 模糊搜尋 可以透過食譜的名稱 利用中間字找到食譜
     * @param title 食譜名稱
     * @return 很多食譜
     */
    @Query("SELECT m FROM Recipe m WHERE m.name LIKE %:title%")
    Optional<Iterable<Recipe>> findByPartName(@Param("title") String title);

    Optional<Iterable<Recipe>> findAllByVersion(String version);
    /**
     * 更新的時候會檢查一下是否有重複的名稱 (deprecate)
     * @param name 更新後的食譜名稱
     * @param id 要更新的食譜id
     * @return 更新如果>0代表更新成功
     */
    @Query("SELECT COUNT(i) FROM Recipe i WHERE ( i.name = :name ) AND i.id != :id")
    Long ExistName(@Param("name") String name,  @Param("id") Long id);

    @Query("SELECT i FROM Recipe i WHERE ( i.name = :name ) AND (i.version = :version) AND ( i.id != :id )")
    Optional<Iterable<Recipe>> ExistNameAndVersionToUpdate(@Param("name") String name,@Param("version") String version,  @Param("id") Long id);

    @Query("SELECT i FROM Recipe i WHERE ( i.name = :name ) AND (i.version = :version)")
    Optional<Iterable<Recipe>> ExistNameAndVersionToCreate(@Param("name") String name,@Param("version") String version);

}
