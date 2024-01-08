package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.ArticleComplexeDTO;
import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticleSimpleDTO;
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
import java.util.*;
import java.util.stream.Collectors;


/**
 * Manage the requests linked to an article
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private ArticleService articleService;
    private UserService userService;

    private ThemeService themeService;

    /**
     * Retrieves all articles for the authenticated user, sorted by creation date in descending order.
     *
     * @param principal The authenticated user's Principal object.
     * @return ResponseEntity containing a map with a list of sorted articles.
     * @see ArticleService#findArticlesByThemesWithDetails(Set)
     */
    @GetMapping
    public ResponseEntity<?> findAll(Principal principal) {
        String email = principal.getName();

        Optional<User> user = userService.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Set<ArticleSimpleDTO> articles = articleService.findArticlesByThemesWithDetails(user.get().getThemes());

        List<ArticleSimpleDTO> sortedArticles = articles.stream()
                .sorted(Comparator.comparing(ArticleSimpleDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return new ResponseEntity<>(Map.of("articles", sortedArticles), HttpStatus.OK);
    }

    /**
     * Retrieves a specific article by its ID.
     *
     * @param id The ID of the article to retrieve.
     * @return ResponseEntity containing the requested article information.
     * @see ArticleService#getById(Long)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id){
        Article articleFromDB = articleService.getById(id).orElse(null);


        if(articleFromDB == null) {
            log.error("Error : id Article doesn't exist in the Data Base.");
            return ResponseEntity.badRequest().body("User not found");        }

        log.info("Returning the article's informations");
        return new ResponseEntity<>(new ArticleComplexeDTO(articleFromDB), HttpStatus.OK);
    }

    /**
     * Adds a new article for the authenticated user.
     *
     * @param articleDTO  Data Transfer Object containing article information.
     * @param principal   The authenticated user's Principal object.
     * @return ResponseEntity containing information about the newly added article.
     * @see ArticleService#save(Article)
     */
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
