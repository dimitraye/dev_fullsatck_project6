package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.UserDetailsDTO;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Manage the requests linked to a user
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    UserService userService;
    ThemeService themeService;

    PasswordEncoder passwordEncoder;

    /**
     * Handles user registration by saving the user information to the database.
     *
     * @param user User object containing registration information.
     * @return ResponseEntity with a token if registration is successful.
     * @see UserService#findByEmail(String)
     * @see UserService#save(User)
     * @see UserService#generateToken(String)
     */
    @PostMapping("/register")
    public ResponseEntity<?> save(@RequestBody User user) {

        if (StringUtils.isEmpty(user.getEmail()) ||
                StringUtils.isEmpty(user.getUserName()) ||
                StringUtils.isEmpty(user.getPassword()) ) {
            return ResponseEntity.badRequest().body("A user field is missing or empty.");
        }

        Optional<User> userFromDB = userService.findByEmail(user.getEmail());

        if (userFromDB.isPresent()) {
            return ResponseEntity.badRequest().body("Email is already used");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userService.save(user);
            //Envoie un message sur la page de redirection
        } catch (Exception e) {
            log.error("Unable to save user.", e);
            return ResponseEntity.badRequest().body("Unable to save user.");
        }

        String generateToken = userService.generateToken(user.getEmail());
        return ResponseEntity.ok(Collections.singletonMap("token", generateToken));
    }

    /**
     * Handles user login by validating credentials and providing a token upon successful login.
     *
     * @param loginForm Map containing user email and password.
     * @return ResponseEntity with a token if login is successful.
     * @see UserService#findByEmail(String)
     * @see PasswordEncoder#matches(CharSequence, String)
     * @see UserService#generateToken(String)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginForm) {
        String email = loginForm.get("email");
        String password = loginForm.get("password");

        if (StringUtils.isEmpty(email) ||
                StringUtils.isEmpty(password) ) {
            return ResponseEntity.badRequest().body("A user field is missing or empty.");
        }

        Optional<User> userFromDB = userService.findByEmail(email);

        if (!userFromDB.isPresent()) {
            return ResponseEntity.badRequest().body("User isn't registered.");
        }

        if (!passwordEncoder.matches(password, userFromDB.get().getPassword())){
            return ResponseEntity.badRequest().body("Email or Password is incorrect.");
        }

        String generateToken = userService.generateToken(email);
        return ResponseEntity.ok(Collections.singletonMap("token", generateToken));
    }

    /**
     * Retrieves user details for the authenticated user.
     *
     * @param principal The authenticated user's Principal object.
     * @return ResponseEntity with user details if the user is found.
     * @see UserService#findByEmail(String)
     * @see UserDetailsDTO
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        String email = principal.getName();

        Optional<User> user = userService.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        return new ResponseEntity<>(new UserDetailsDTO(user.get()), HttpStatus.OK);
    }

    /**
     * Adds a theme subscription for the authenticated user.
     *
     * @param id        The ID of the theme to subscribe to.
     * @param principal The authenticated user's Principal object.
     * @return ResponseEntity with a message if the subscription is successful.
     * @see UserService#findByEmail(String)
     * @see ThemeService#findById(Long)
     * @see UserService#save(User)
     */
    @GetMapping("/themes/{id}")
    public ResponseEntity<?> addTheme(@PathVariable Long id, Principal principal) {

        String email = principal.getName();

        Optional<User> user = userService.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Optional<Theme> themeFromDB = themeService.findById(id);

        if (themeFromDB.isEmpty()) {
            return ResponseEntity.badRequest().body("Theme not found");
        }

        user.get().getThemes().add(themeFromDB.get());

        try {
            userService.save(user.get());
            return ResponseEntity.ok(Map.of("message", "Abonnement créé"));
        } catch (Exception e) {
            log.error("Unable to save user.", e);
            return ResponseEntity.badRequest().body("Unable to save user.");
        }
    }


    /**
     * Retrieves user details for a specified user ID.
     *
     * @param id The ID of the user to retrieve details for.
     * @return ResponseEntity with user details if the user is found.
     * @see UserService#getUserById(Long)
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable Long id) {
        Optional<User> userFromDBOpt = userService.getUserById(id);

        if (userFromDBOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User userFromDB = userFromDBOpt.get();

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setId(userFromDB.getId());
        userDetailsDTO.setEmail(userFromDB.getEmail());
        userDetailsDTO.setUserName(userFromDB.getUserName());
        userDetailsDTO.setThemes(userFromDB.getThemes());

        return ResponseEntity.ok(userDetailsDTO);
    }

    /**
     * Updates user information for a specified user ID.
     *
     * @param id   The ID of the user to update.
     * @param user User object containing updated user information.
     * @return ResponseEntity with the updated user information if successful.
     * @see UserService#getUserById(Long)
     * @see UserService#update(User)
     */
    @PutMapping("/user/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody User user) {

        Optional<User> userFromDBOpt = userService.getUserById(id);

        if(userFromDBOpt.isEmpty()) {
            log.error("Error : User already  exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User userFromDB = userFromDBOpt.get();

        log.debug("Set properties in the object userFromDB.");
        userFromDB.setUserName(user.getUserName());
        userFromDB.setEmail(user.getEmail());

        log.info("Updating a user");
        return new ResponseEntity<>(userService.update(userFromDB), HttpStatus.OK);
    }

    /**
     * Removes a theme subscription for the authenticated user.
     *
     * @param id        The ID of the theme to unsubscribe from.
     * @param principal The authenticated user's Principal object.
     * @return ResponseEntity with a message if the unsubscription is successful.
     * @see UserService#findByEmail(String)
     * @see ThemeService#findById(Long)
     * @see UserService#save(User)
     */
    @PutMapping("/themes/{id}")
    public ResponseEntity<?> removeTheme(@PathVariable Long id, Principal principal) {

        String email = principal.getName();

        Optional<User> user = userService.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Optional<Theme> themeFromDB = themeService.findById(id);

        if (themeFromDB.isEmpty()) {
            return ResponseEntity.badRequest().body("Theme not found");
        }

        Set<Theme> themes = user.get().getThemes();

        long idToDelete = id;

        themes = themes.stream()
                .filter(theme -> theme.getId() != idToDelete)
                .collect(Collectors.toSet());

        user.get().setThemes(themes);

        try {
            userService.save(user.get());
            return ResponseEntity.ok(Map.of("message", "Abonnement supprimé"));
        } catch (Exception e) {
            log.error("Unable to delete the user's theme", e);
            return ResponseEntity.badRequest().body("Unable to save user.");
        }
    }

    /**
     * Retrieves the list of themes subscribed by the authenticated user.
     *
     * @param principal The authenticated user's Principal object.
     * @return ResponseEntity with the list of subscribed theme names.
     * @see UserService#findByEmail(String)
     */
    @GetMapping("/user/themes")
    public ResponseEntity<?> getUserThemes(Principal principal) {
        String email = principal.getName();

        Optional<User> user = userService.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Set<Theme> themes = user.get().getThemes();
        List<String> themeNames = themes.stream()
                .map(Theme::getTitle)
                .collect(Collectors.toList());

        return ResponseEntity.ok(themeNames);
    }
}
