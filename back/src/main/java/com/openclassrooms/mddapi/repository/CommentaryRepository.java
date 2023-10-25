package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long> {

    List<Commentary> findAllByArticle(Article article);

    List<Commentary> findAllByArticle_Id(Long id);
}
