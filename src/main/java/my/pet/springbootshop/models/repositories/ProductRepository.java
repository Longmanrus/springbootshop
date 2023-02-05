package my.pet.springbootshop.models.repositories;

import my.pet.springbootshop.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}