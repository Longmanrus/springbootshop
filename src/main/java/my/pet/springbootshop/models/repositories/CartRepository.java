package my.pet.springbootshop.models.repositories;

import my.pet.springbootshop.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findCartsByUserUsername(String username);
    List<Cart> findAllByUserId(long id);




}