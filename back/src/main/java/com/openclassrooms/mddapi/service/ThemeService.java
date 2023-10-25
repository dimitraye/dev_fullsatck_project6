package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ThemeService {
    private ThemeRepository themeRepository;

    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    public Optional<Theme> findById(Long id) {
        return themeRepository.findById(id);
    }
}
