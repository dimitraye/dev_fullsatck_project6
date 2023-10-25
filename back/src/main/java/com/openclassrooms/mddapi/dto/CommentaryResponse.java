package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.model.Commentary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentaryResponse {
    private String content;
    private Long user_id;

    private Long article_id;


    public CommentaryResponse(Commentary commentary) {
        this.content = commentary.getContent();
        if (commentary.getUser() != null) {
            this.user_id = commentary.getUser().getId();
        }
        if (commentary.getArticle() != null) {
            this.article_id = commentary.getArticle().getId();
        }
    }
}