package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openclassrooms.mddapi.dto.ArticleDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    @JsonBackReference
    private Theme theme;

    @OneToMany(mappedBy = "article")
    @JsonIgnore
    private Set<Commentary> commentaries = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    public Article(ArticleDTO article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
