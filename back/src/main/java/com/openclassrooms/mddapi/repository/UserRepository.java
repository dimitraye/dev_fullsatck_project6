package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by its username(email).
     * @param email
     * @return the user.
     */
    Optional<User> findByEmail (String email);

    @Query(value = "SELECT * FROM users u " +
            "LEFT JOIN user_themes ut ON u.id = ut.user_id " +
            "LEFT JOIN theme t ON ut.theme_id = t.id " +
            "WHERE u.email = :email", nativeQuery = true)
    Optional<User> findByEmailWithThemes (@Param("email") String email);
}
