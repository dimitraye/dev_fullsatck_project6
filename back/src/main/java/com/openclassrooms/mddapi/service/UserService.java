package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Service class for managing user-related operations.
 */
@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;
    private final JwtEncoder encoder;


    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return An {@link Optional} containing the user with the given ID, or empty if not found.
     * @see UserRepository#findById(Long)
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Saves a new user or updates an existing one.
     *
     * @param user The user to be saved or updated.
     * @return The saved or updated user.
     * @see UserRepository#save(User)
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return An {@link Optional} containing the user with the given email, or empty if not found.
     * @see UserRepository#findByEmail(String)
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Updates user information.
     *
     * @param user The user object with updated information.
     * @return The updated user.
     * @see #save(User)
     */
    public User update(User user) {
        return save(user);
    }

    /**
     * Generates a JWT token for the specified email address.
     *
     * @param email The email address for which to generate the token.
     * @return The JWT token as a string.
     */
    public String generateToken(String email ) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("security-service")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.HOURS))
                .subject(email)
                .claim("scope", "read")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
