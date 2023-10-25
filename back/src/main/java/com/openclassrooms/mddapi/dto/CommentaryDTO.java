package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.model.Commentary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentaryDTO {
    private String content;
    private String userName;


    public CommentaryDTO(Commentary commentary) {
        this.content = commentary.getContent();
        this.userName = commentary.getUser().getUserName();
    }
}