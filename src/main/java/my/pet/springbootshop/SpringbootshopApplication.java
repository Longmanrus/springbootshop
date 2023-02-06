package my.pet.springbootshop;

import my.pet.springbootshop.models.entities.Cart;
import my.pet.springbootshop.models.enums.Role;
import my.pet.springbootshop.models.entities.Category;
import my.pet.springbootshop.models.entities.Product;
import my.pet.springbootshop.models.entities.User;
import my.pet.springbootshop.models.repositories.CartRepository;
import my.pet.springbootshop.models.repositories.CategoryRepository;
import my.pet.springbootshop.models.repositories.ProductRepository;
import my.pet.springbootshop.models.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

@SpringBootApplication
public class SpringbootshopApplication {

	private final UsersRepository usersRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final CartRepository cartRepository;
	@Autowired
	public SpringbootshopApplication(UsersRepository usersRepository, ProductRepository productRepository, CategoryRepository categoryRepository,
									 CartRepository cartRepository) {
		this.usersRepository = usersRepository;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.cartRepository = cartRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootshopApplication.class, args);
	}
	@Bean
	public CommandLineRunner testData(UsersRepository usersRepository) {
		return (args) -> {

			User user1 = new User();
			user1.setEmail("admin@admin.com");
			user1.setPassword("$2a$12$eFuLCSyxws4At0sfYmhODe4lAMEsp2ZcQaspVAob/Hkcexfc26ibK");
			user1.setUsername("admin");
			user1.setRole(Role.ADMIN);
			User user2 = new User();
			user2.setEmail("user@user.com");
			user2.setPassword("$2a$12$HkzRVrg0Zql8KfZaxz1UbebihueW3VZ/WtrfjzsZnBrrrrTywHgjS");
			user2.setUsername("user");

			Category cater1 = new Category();
			cater1.setTitle("Телевизоры");
			Category cater2 = new Category();
			cater2.setTitle("Телефоны");
			Category cater3 = new Category();
			cater3.setTitle("Клавиатуры");

			Product product1 = new Product();
			product1.setTitle("Philips B100500");
			product1.setDescription("Телевизор филипс бла бла бла");
			product1.setImage("image1.jpg");
			product1.setPrice(431244);
			product1.setCount(4);
			product1.setCategory(cater1);

			Product product2 = new Product();
			product2.setTitle("Thing 2020");
			product2.setDescription("Штука без штуки");
			product2.setImage("image2.jpg");
			product2.setPrice(3333);
			product2.setCount(2);
			product2.setCategory(cater3);

			Cart cart1 = new Cart();
			cart1.setUser(user1);
			cart1.setProduct(product1);
			cart1.setCount(4);
			Cart cart2 = new Cart();
			cart2.setUser(user1);
			cart2.setProduct(product2);
			cart2.setCount(3);




			try {
				usersRepository.save(user1);
				usersRepository.save(user2);

				categoryRepository.save(cater1);
				categoryRepository.save(cater2);
				categoryRepository.save(cater3);

				productRepository.save(product1);
				productRepository.save(product2);

				cartRepository.save(cart1);
				cartRepository.save(cart2);

			} catch (Exception e){
				System.out.println("Тестовые данные уже есть в DB");
			}





		};
	}

}
