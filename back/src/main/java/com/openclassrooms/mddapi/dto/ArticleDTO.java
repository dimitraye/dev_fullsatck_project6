package com.openclassrooms.mddapi.dto;



import com.openclassrooms.mddapi.model.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private Long theme_id;

    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        if (article.getTheme() != null) {
            this.theme_id = article.getTheme().getId();
        }
    }
}
