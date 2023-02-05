package my.pet.springbootshop.rest_controllers;

import my.pet.springbootshop.models.entities.Category;
import my.pet.springbootshop.models.entities.Product;
import my.pet.springbootshop.models.entities.User;
import my.pet.springbootshop.models.repositories.CategoryRepository;
import my.pet.springbootshop.models.repositories.ProductRepository;
import my.pet.springbootshop.models.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
    В основном классе Application есть метод забивающий бд тестовыми данными
    Аутентификация происходит посредством получения JWT токена и помещении его в header Authorization для каждого запроса.
 */
@RestController
@RequestMapping("/api")
public class MainController {

    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public MainController(UsersRepository usersRepository, ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }



    //тестовый метод для образца получения информации о авторизованном пользователе
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getAllUsers(Authentication authentication){

        Optional<User> user = usersRepository.findByUsername(authentication.getName());
        if (user.isPresent()) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //вывод всех продуктов
    @GetMapping("/products")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getAllProducts(){
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // добавление продукта
    @PostMapping("/products")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Product> addProduct(@RequestBody Product product, @RequestParam("category_id") long catId){
        product.setCategory(categoryRepository.findById(catId).orElse(null));
        productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    // изменение количества продукта
    @PatchMapping("/products")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Product> addProduct(@RequestParam("product_id") long id,
                                              @RequestParam("count") int count){
        if(productRepository.findById(id).isPresent()){
            Product product = productRepository.findById(id).get();
            product.setCount(count);
            productRepository.save(product);
            return new ResponseEntity<>(HttpStatus.OK);

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Получение всех категорий с вложенными сетами всех товаров в этих категориях
    @GetMapping("/categories")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getAllCategory(){
        List<Category> categories = categoryRepository.findAll();
        if (!categories.isEmpty()) {
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Получение категории по названию с сетом товаров принадлежащей ей.
    @GetMapping("/category")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getCategoryByTitle(@RequestParam("title") String title){
        Optional<Category> category = categoryRepository.findByTitle(title);
        if (category.isPresent()) {
            return new ResponseEntity<>(category, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Создание категории
    @PostMapping("/category")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }




}
