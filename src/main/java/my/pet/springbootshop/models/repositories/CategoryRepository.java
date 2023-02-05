package my.pet.springbootshop.models.repositories;

import my.pet.springbootshop.models.entities.Category;
import my.pet.springbootshop.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByTitle(String Title);
}