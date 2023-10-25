package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * Manage the requests linked to a user
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    ArticleService articleService;
    UserService userService;

    ThemeService themeService;


    /**
     * Find all the rentals when calling this endpoint
     * @return a list of rentals
     */
    @GetMapping
    public ResponseEntity<?> findAll(Principal principal) {
        String email = principal.getName();

        Optional<User> user = userService.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Set<Object[]> articles = articleService.findArticlesByThemesWithDetails(user.get().getThemes());

        //List<ArticleComplexeDTO> articlesCompDto = articles.stream().map(article -> new ArticleComplexeDTO(article)).collect(Collectors.toList());
        return new ResponseEntity<>(Map.of("articles", articles), HttpStatus.OK);
    }

    /*@GetMapping
    public ResponseEntity<List<Article>> findAll() {
        List<Article> articles = articleService.getAll();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id){
        Article articleFromDB = articleService.getById(id).orElse(null);

        if(articleFromDB == null) {
            log.error("Error : id Article doesn't exist in the Data Base.");
            return ResponseEntity.badRequest().body("User not found");        }

        log.info("Returning the article's informations");
        return new ResponseEntity<>(articleFromDB, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addArticle(@RequestBody ArticleDTO articleDTO, Principal principal) {

        String email = principal.getName();
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }


        if (StringUtils.isEmpty(articleDTO.getTitle()) ||
                StringUtils.isEmpty(articleDTO.getContent()) ||
        StringUtils.isEmpty(articleDTO.getTheme_id())) {
            return ResponseEntity.badRequest().body("An article field is missing or empty.");
        }

        Optional<Theme> theme = themeService.findById(articleDTO.getTheme_id());
        if (theme.isEmpty()) {
            return ResponseEntity.badRequest().body("theme not found");
        }

        Article article = new Article(articleDTO);
        article.setTheme(theme.get());
        article.setUser(user.get());

        log.info("Saving the new article");
        Article articleFromDB = null;
        try {
            articleFromDB = articleService.save(article);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unable to save the articl");
        }

        return  new ResponseEntity<>(Map.of("article", new ArticleDTO(articleFromDB)), HttpStatus.CREATED);
    }


}
