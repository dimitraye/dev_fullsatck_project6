package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.service.ThemeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * Manage the requests linked to a Theme
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private ThemeService themeService;

    /**
     * Retrieves all themes when calling this endpoint.
     *
     * @param principal The authenticated user's Principal object.
     * @return ResponseEntity with a list of themes if retrieval is successful.
     * @see ThemeService#getAll()
     */
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Theme> themes = themeService.getAll();
        return new ResponseEntity<>(Map.of("themes",themes), HttpStatus.OK);
    }

    /**
     * Adds a new theme using the provided theme object.
     *
     * @param theme The Theme object containing information about the theme to be added.
     * @return ResponseEntity with the added theme if successful.
     * @see ThemeService#save(Theme)
     */
    @PostMapping
    public ResponseEntity<?> addMessage(@RequestBody Theme theme) {
        themeService.save(theme);
        return  new ResponseEntity<>(Map.of("Theme :",theme), HttpStatus.CREATED);
    }
}
