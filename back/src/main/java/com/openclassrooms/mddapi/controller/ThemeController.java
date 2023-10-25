package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.model.Commentary;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.service.ThemeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;


/**
 * Manage the requests linked to a user
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    ThemeService themeService;

    /**
     * Find all the rentals when calling this endpoint
     * @return a list of rentals
     */
    @GetMapping
    public ResponseEntity<?> findAll(Principal principal) {
        List<Theme> themes = themeService.getAll();
        return new ResponseEntity<>(Map.of("themes",themes), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addMessage(@RequestBody Theme theme) {
        themeService.save(theme);
        return  new ResponseEntity<>(Map.of("Theme :",theme), HttpStatus.CREATED);
    }
}
