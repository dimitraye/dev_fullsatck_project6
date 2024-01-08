package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Theme-related operations.
 */
@AllArgsConstructor
@Service
public class ThemeService {
    private ThemeRepository themeRepository;

    /**
     * Retrieves a list of all themes.
     *
     * @return List of all themes available in the system.
     * @see ThemeRepository#findAll()
     */
    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    /**
     * Saves a new theme or updates an existing one.
     *
     * @param theme The theme to be saved or updated.
     * @return The saved or updated theme.
     * @see ThemeRepository#save(Theme)
     */
    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    /**
     * Retrieves a theme by its ID.
     *
     * @param id The ID of the theme to retrieve.
     * @return An {@link Optional} containing the theme with the given ID, or empty if not found.
     * @see ThemeRepository#findById(Long)
     */
    public Optional<Theme> findById(Long id) {
        return themeRepository.findById(id);
    }
}
