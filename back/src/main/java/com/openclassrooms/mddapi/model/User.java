package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private  String userName;
    private String password;

    @OneToMany
    @JsonIgnore
    private Set<Theme> themes = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Article> articles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Commentary> commentaries = new HashSet<>();
    @CreatedDate
    private LocalDateTime createdAt;
}