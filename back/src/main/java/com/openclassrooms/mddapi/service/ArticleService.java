package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.dto.ArticleSimpleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing user-related operations.
 */
@AllArgsConstructor
@Service
public class ArticleService {

    private ArticleRepository articleRepository;

    /**
     * Retrieves a set of article details (simple DTO) based on the given themes.
     *
     * @param themes Set of themes to filter articles.
     * @return Set of article details (simple DTO) matching the provided themes.
     * @see ArticleRepository#findArticlesByThemesWithDetails(Set)
     */
    public Set<ArticleSimpleDTO> findArticlesByThemesWithDetails(Set<Theme> themes) {
        return  articleRepository.findArticlesByThemesWithDetails(themes);
    }

    /**
     * Saves a new article or updates an existing one.
     *
     * @param article The article to be saved or updated.
     * @return The saved or updated article.
     * @see ArticleRepository#save(Article)
     */
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    /**
     * Retrieves an article by its ID.
     *
     * @param id The ID of the article to retrieve.
     * @return Optional containing the article if found.
     * @see ArticleRepository#findById(Long)
     */
    public Optional<Article> getById(Long id) {
        return articleRepository.findById(id);
    }
}
