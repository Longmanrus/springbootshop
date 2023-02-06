package my.pet.springbootshop.models.repositories;

import my.pet.springbootshop.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}