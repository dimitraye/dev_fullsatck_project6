package com.openclassrooms.mddapi;

import com.openclassrooms.mddapi.config.RsaKeyProperties;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class MddApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MddApiApplication.class, args);
	}


	@Bean
	CommandLineRunner runner(PasswordEncoder encoder,
							 UserService userService,
							 ThemeService themeService,
							 ThemeRepository themeRepository,
							 ArticleService articleService) {

		return args -> {
			//--------------
			/*List<User> users = new ArrayList<>();
			List<User> usersFromDB = new ArrayList<>();
			List<Rental> rentals = new ArrayList<>();


			List<String> firstnames = List.of("user", "admin", "Jean", "Anna", "Olivier", "Sophie", "Omar", "Greg");
			for (int i =0; i < 8; i++) {
				User user = new User();
				user.setName(firstnames.get(i));
				user.setPassword(firstnames.get(i));
				user.setEmail(firstnames.get(i).toLowerCase() + "@mail.com");
				//user.setPassword(encoder.encode(firstnames.get(i)));
				log.info("Tour: " + i);
				log.info("user:"+ user.getEmail());
				users.add(user);
			}
			try {
				log.info("users:"+ users);
				users.forEach(user -> {
					User userFromDB = userService.save(user);
					System.out.println("User saved, " + userFromDB);

					usersFromDB.add(userFromDB);
				});
				log.info("usersFromDB:"+ usersFromDB);

				System.out.println("Users Saved!");

			} catch (Exception e){
				System.out.println("Unable to save users: " + e.getMessage());
			}*/

			List<Theme> themes = generateSampleThemes();
			themeRepository.saveAll(themes);

		};
	}

	public List<Theme> generateSampleThemes() {
		List<Theme> themes = new ArrayList<>();

		Theme theme1 = new Theme();
		theme1.setTitle("Science");
		theme1.setDescription("Discussions about various scientific topics.");

		Theme theme2 = new Theme();
		theme2.setTitle("Technology");
		theme2.setDescription("All about the latest technological advancements.");

		Theme theme3 = new Theme();
		theme3.setTitle("Environment");
		theme3.setDescription("Concerns related to the environment and sustainability.");

		// Add more themes as needed

		themes.add(theme1);
		themes.add(theme2);
		themes.add(theme3);

		return themes;
	}


}
