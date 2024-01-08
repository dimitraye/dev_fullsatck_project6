package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.dto.ArticleSimpleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Repository interface for managing article-related database operations.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT NEW com.openclassrooms.mddapi.dto.ArticleSimpleDTO(a.id, a.title, a.content, a.createdAt, u.userName) " +
            "FROM Article a " +
            "JOIN a.theme t " +
            "JOIN a.user u " +
            "WHERE t IN :themes")
    Set<ArticleSimpleDTO> findArticlesByThemesWithDetails(@Param("themes") Set<Theme> themes);
}
