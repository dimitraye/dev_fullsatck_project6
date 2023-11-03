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
     * Manage the user registration
     * @param user

     * @return
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

        //sauvegarde du user
        try {
            userService.save(user);
            //Envoie un message sur la page de redirection
        } catch (Exception e) {
            log.error("Unable to save user.", e);
            return ResponseEntity.badRequest().body("Unable to save user.");
        }

        //creation du token
        String generateToken = userService.generateToken(user.getEmail());
        return ResponseEntity.ok(Collections.singletonMap("token", generateToken));
    }


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

        //Vérification du password
        if (!passwordEncoder.matches(password, userFromDB.get().getPassword())){
            return ResponseEntity.badRequest().body("Email or Password is incorrect.");
        }

        //creation du token
        String generateToken = userService.generateToken(email);
        return ResponseEntity.ok(Collections.singletonMap("token", generateToken));
    }

    /**
     * Return the login  page
     * @param principal
     * @return
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
            //Envoie un message sur la page de redirection
            //creation du token
            return ResponseEntity.ok(Map.of("message", "Abonnement créé"));
        } catch (Exception e) {
            log.error("Unable to save user.", e);
            return ResponseEntity.badRequest().body("Unable to save user.");
        }


    }


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

    //TODO : removeFromSub
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

        Set<Theme> themes = user.get().getThemes();/* initialisez votre liste ici */;

        long idToDelete = id; // Remplacez 42 par l'ID de l'objet que vous souhaitez supprimer

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
