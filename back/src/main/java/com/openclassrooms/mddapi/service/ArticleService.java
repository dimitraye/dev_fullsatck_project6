package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.dto.ArticleSimpleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class ArticleService {

    private ArticleRepository articleRepository;

    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    public Set<Article> findByThemeIn(Set<Theme> themes) {
        return articleRepository.findArticlesByThemes(themes);
    }

    public Set<ArticleSimpleDTO> findArticlesByThemesWithDetails(Set<Theme> themes) {
        return  articleRepository.findArticlesByThemesWithDetails(themes);
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Optional<Article> getById(Long id) {
        return articleRepository.findById(id);
    }
}
