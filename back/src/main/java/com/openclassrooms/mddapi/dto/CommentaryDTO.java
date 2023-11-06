package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.Utils.DateTimeConversionUtil;
import com.openclassrooms.mddapi.model.Commentary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentaryDTO {
    private String content;
    private String userName;

    private LocalDateTime createdAt;


    public CommentaryDTO(Commentary commentary) {
        this.content = commentary.getContent();
        this.userName = commentary.getUser().getUserName();
        this.createdAt = commentary.getCreatedAt();
    }
}