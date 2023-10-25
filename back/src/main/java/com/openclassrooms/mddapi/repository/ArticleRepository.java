package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Set<Article> findByThemeIn(Set<Theme> themes);

    @Query(
            value = "SELECT * FROM Article a WHERE a.theme_id IN (SELECT t.id FROM Theme t WHERE t IN :themes)",
            nativeQuery = true
    )
    Set<Article> findArticlesByThemes(@Param("themes") Set<Theme> themes);

    @Query(
            value = "SELECT * FROM Article a WHERE a.theme_id IN :themeIds",
            nativeQuery = true
    )
    Set<Article> findArticlesByThemeIds(@Param("themeIds") List<Long> themeIds);

    @Query("SELECT DISTINCT a.title, a.createdAt, a.content, u.userName " +
            "FROM Article a " +
            "JOIN a.theme t " +
            "JOIN a.user u " +
            "WHERE t IN :themes")
    Set<Object[]> findArticlesByThemesWithDetails(@Param("themes") Set<Theme> themes);
}
