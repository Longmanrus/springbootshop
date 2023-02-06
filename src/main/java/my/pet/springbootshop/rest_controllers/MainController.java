package my.pet.springbootshop.rest_controllers;

import my.pet.springbootshop.models.entities.*;
import my.pet.springbootshop.models.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
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
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    @Autowired
    public MainController(UsersRepository usersRepository, ProductRepository productRepository,
                          CategoryRepository categoryRepository, CartRepository cartRepository,
                          OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
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
    public ResponseEntity<Product> changeProductCount(@RequestParam("product_id") long id,
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
    // Выдача корзины аутентифицированному юзеру
    @GetMapping("/cart")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getUserCart(Authentication authentication){
        List<Cart> cart = cartRepository.findAllByUserId(usersRepository.findByUsername(authentication.getName()).get().getId());
        if (!cart.isEmpty()) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //добавление товара в корзину
    @PostMapping("/cart")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Cart> addToCart(@RequestParam("product_id") long product_id,
                                                @RequestParam("count") int count,
                                                Authentication authentication){
        Cart cart = new Cart();
        cart.setUser(usersRepository.findByUsername(authentication.getName()).orElseThrow(NoSuchElementException::new));
        cart.setProduct(productRepository.findById(product_id).orElse(null));
        cart.setCount(count);
        cartRepository.save(cart);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    // создание заказа
    @PostMapping("/order")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Category> createOrder (Authentication authentication){
        Optional<User> user = usersRepository.findByUsername(authentication.getName());
        Order order = new Order();
        order.setUser(user.orElseThrow(NoSuchElementException::new));
        orderRepository.save(order);
        List<Cart> carts = cartRepository.findAllByUserId(user.get().getId());
        carts.forEach(cart -> {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cart.getProduct());
            orderProduct.setCount(cart.getCount());
            orderProductRepository.save(orderProduct);
        });

        return new ResponseEntity<>(HttpStatus.CREATED);
    }









}
