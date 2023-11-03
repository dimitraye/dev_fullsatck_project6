package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.CommentaryResponse;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Commentary;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.CommentaryService;
import com.openclassrooms.mddapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Manage the requests linked to a user
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/commentaries")
public class MessageController {
    private CommentaryService commentaryService;
    private UserService userService;

    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<?> addMessage(@RequestBody CommentaryResponse commentaryResponse) {

        if (StringUtils.isEmpty(commentaryResponse.getContent()) ||
                StringUtils.isEmpty(commentaryResponse.getUser_id()) ||
                StringUtils.isEmpty(commentaryResponse.getArticle_id()) ) {
            return ResponseEntity.badRequest().body("A commentary field is missing or empty.");
        }
        Optional<User> userFromDB = userService.getUserById(commentaryResponse.getUser_id());

        if (userFromDB.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }


        Optional<Article> articleFromDB = articleService.getById(commentaryResponse.getArticle_id());

        if (articleFromDB.isEmpty()) {
            return ResponseEntity.badRequest().body("article not found");
        }

        Commentary commentary = new Commentary();

        commentary.setUser(userFromDB.get());
        commentary.setArticle((articleFromDB.get()));
        commentary.setContent(commentaryResponse.getContent());


        Commentary commentaryDB = null;
        try {
            commentaryDB = commentaryService.save(commentary);
            //Envoie un message sur la page de redirection
        } catch (Exception e) {
            log.error("Unable to save commentary.", e);
            return ResponseEntity.badRequest().body("Unable to save commentary.");
        }
        return  new ResponseEntity<>(Map.of("message", "Commentaire ajouté"), HttpStatus.CREATED);
    }

    //TODO : getCommentariesByArticleId
    @GetMapping("/{id}")
    public ResponseEntity<List<Commentary>> findAllCommentariesByArticleId(@PathVariable Long id) {
        List<Commentary> commentaries = commentaryService.getAllByArticle_Id(id);
        return ResponseEntity.ok(commentaries);
    }
}
