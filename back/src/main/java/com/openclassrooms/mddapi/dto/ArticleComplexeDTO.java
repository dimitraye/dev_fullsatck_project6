package com.openclassrooms.mddapi.dto;


import com.openclassrooms.mddapi.Utils.DateTimeConversionUtil;
import com.openclassrooms.mddapi.model.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleComplexeDTO {
    private Long id;
    private String title;
    private String content;
    private String theme;
    private String createdAt;
    private String userName;
    private Set<CommentaryDTO> commentaries = new HashSet<>();

    public ArticleComplexeDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content  = article.getContent();
        if (article.getTheme() != null) {
            this.theme = article.getTheme().getTitle();
        }
        this.createdAt = DateTimeConversionUtil.
                convertLocalDateTimeToString(article.getCreatedAt());
        this.userName = article.getUser().getUserName();

        if (!CollectionUtils.isEmpty(article.getCommentaries())) {
            article.getCommentaries().forEach(commentary -> {
                commentaries.add(new CommentaryDTO(commentary));
            });
        }
    }
}
