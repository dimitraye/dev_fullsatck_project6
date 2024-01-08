package com.openclassrooms.mddapi.dto;


import com.openclassrooms.mddapi.Utils.DateTimeConversionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSimpleDTO {
    private Long id;
    private String title;
    private String content;
    private String createdAt;
    private String userName;

    public ArticleSimpleDTO(long id, String title, String content, LocalDateTime createdAt, String userName) {
        this.id = id;
        this.title = title;
        this.content  = content;
        this.createdAt = DateTimeConversionUtil.
                convertLocalDateTimeToString(createdAt);
        this.userName = userName;
    }

}
