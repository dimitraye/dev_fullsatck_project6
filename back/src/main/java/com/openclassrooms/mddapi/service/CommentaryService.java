package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Commentary;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentaryService {

    private ArticleRepository articleRepository;
    private CommentaryRepository commentaryRepository;

    public List<Commentary> getAllByArticle(Article article) {
        return commentaryRepository.findAllByArticle(article);
    }

    public List<Commentary> getAllByArticle_Id(Long id) {
        return commentaryRepository.findAllByArticle_Id(id);
    }
    public Commentary save(Commentary commentary) {
        return commentaryRepository.save(commentary);
    }

}
