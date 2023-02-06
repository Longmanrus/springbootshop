package my.pet.springbootshop.models.repositories;

import my.pet.springbootshop.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Base64;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername (String username);
    Boolean existsByEmail (String email);

}