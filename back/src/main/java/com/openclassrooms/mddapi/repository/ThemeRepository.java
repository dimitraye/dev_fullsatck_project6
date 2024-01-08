package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing theme-related database operations.
 */
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

}
