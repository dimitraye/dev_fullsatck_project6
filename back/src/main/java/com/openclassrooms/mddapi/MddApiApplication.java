package com.openclassrooms.mddapi;

import com.openclassrooms.mddapi.config.RsaKeyProperties;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Commentary;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentaryRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class MddApiApplication {

	@Autowired
	private PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(MddApiApplication.class, args);
	}


	@Bean
	CommandLineRunner runner(UserService userService,
							 ThemeService themeService,
							 ThemeRepository themeRepository,
							 ArticleService articleService,
							 ArticleRepository articleRepository,
							 UserRepository userRepository,
							 CommentaryRepository commentaryRepository) {

		return args -> {

			///////////////////////////////GENERATION THEMES/////////////////////////////////////

			Set<Theme> themes = generateSampleThemes();
			List<Theme> savedThemeList = themeRepository.saveAll(themes);
			Set<Theme> savedThemes = new HashSet<>(savedThemeList);

			///////////////////////////////GENERATION USERS/////////////////////////////////////
			List<User> users = generateSampleUsers();
			// Nombre d'éléments aléatoires à extraire pour chaque utilisateur
			int numRandomThemes = 2; // Vous pouvez modifier cela selon vos besoins

			// Attribution des thèmes aléatoires à chaque utilisateur
			users.forEach(user -> {
				List<Theme> randomThemes = new ArrayList<>(savedThemes); // Créez une copie de savedThemes

				// Mélangez la liste des thèmes
				Collections.shuffle(randomThemes);

				// Extrayez un certain nombre de thèmes aléatoires
				List<Theme> randomThemes1 = randomThemes.subList(0, numRandomThemes);
				user.setThemes(new HashSet<>(randomThemes1));
			});
			System.out.println("---------------users before save");
			users.forEach(user -> {
				System.out.println(user);
				user.getThemes().forEach(theme -> System.out.println(theme));
			});
			List<User> savedUsers = userRepository.saveAll(users);

			///////////////////////////////GENERATION Articles/////////////////////////////////////
			List<Article> articles = generateSampleArticles();
			// Générateur de nombres aléatoires
			Random random = new Random();

			articles.forEach(article -> {
				// Récupérez un thème aléatoire
				Theme randomTheme = savedThemeList.get(random.nextInt(savedThemeList.size()));
				User randomUser = savedUsers.get(random.nextInt(savedUsers.size()));
				article.setTheme(randomTheme);
				article.setUser(randomUser);
			});
			List<Article> savedArticles = articleRepository.saveAll(articles);

			///////////////////////////////GENERATION Commentaries/////////////////////////////////////
			List<Commentary> commentaries = generateSampleCommentaries();
			commentaries.forEach(commentary -> {
				Article randomArticle = savedArticles.get(random.nextInt(savedArticles.size()));
				User randomUser = savedUsers.get(random.nextInt(savedUsers.size()));
				commentary.setArticle(randomArticle);
				commentary.setUser(randomUser);
			});
			commentaryRepository.saveAll(commentaries);
		};
	}

	public Set<Theme> generateSampleThemes() {
		Set<Theme> themes = new HashSet<>();

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
	public List<User> generateSampleUsers() {
		List<User> users = new ArrayList<>();

		List<String> userNames = List.of("user", "admin", "Jean", "Anna", "Olivier", "Sophie", "Omar", "Greg");
		for (int i =0; i < 8; i++) {
			User user = new User();
			user.setUserName(userNames.get(i));
			user.setEmail(userNames.get(i).toLowerCase() + "@mail.com");
			user.setPassword(encoder.encode(userNames.get(i).toLowerCase()));
			users.add(user);
		}
		users.forEach(user -> System.out.println(user));
		return users;
	}

	public List<Article> generateSampleArticles() {
		List<Article> articles = new ArrayList<>();
		Random random = new Random();

		for (int i = 1; i <= 5; i++) {
			Article article = new Article();
			article.setTitle("Article " + i);
			article.setContent("Contenu de l'article " + i);

			// Générer des dates aléatoires pour le jour et le mois
			int randomYear = 2023; // L'année reste la même
			int randomMonth = random.nextInt(12) + 1; // De 1 à 12 (mois)
			int randomDay = random.nextInt(28) + 1; // De 1 à 28 (jours)

			LocalDate randomDate = LocalDate.of(randomYear, randomMonth, randomDay);
			article.setCreatedAt(randomDate.atStartOfDay());

			articles.add(article);
		}

		return articles;
	}

	public List<Commentary> generateSampleCommentaries() {
		List<Commentary> commentaries = new ArrayList<>();

		for (int i = 1; i <= 12; i++) {
			Commentary commentary = new Commentary();
			commentary.setContent("Contenu du commentaire " + i);
			commentaries.add(commentary);
		}

		return commentaries;
	}


	// TODO - Faire le sorting des articles en js/ts et des commentaires
	// TODO - changer niveau back/front la fonction me pour qu'elle retrouve le user via l'id
	//  ou rafraichir le Token si on change l'email
	//   ou
}
