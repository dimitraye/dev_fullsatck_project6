package com.openclassrooms.mddapi.dto;


import com.openclassrooms.mddapi.Utils.DateTimeConversionUtil;
import com.openclassrooms.mddapi.model.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSimpleDTO {
    private Long id;
    private String title;
    private String content;

    private String createdAt;

    private String userName;

    public ArticleSimpleDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content  = article.getContent();
        this.createdAt = DateTimeConversionUtil.
                convertLocalDateTimeToString(article.getCreatedAt());
        this.userName = article.getUser().getUserName();
    }

    public ArticleSimpleDTO(long id, String title, String content, LocalDateTime createdAt, String userName) {
        this.id = id;
        this.title = title;
        this.content  = content;
        this.createdAt = DateTimeConversionUtil.
                convertLocalDateTimeToString(createdAt);
        this.userName = userName;
    }

}
