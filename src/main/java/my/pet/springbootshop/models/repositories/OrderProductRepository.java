package my.pet.springbootshop.models.repositories;

import my.pet.springbootshop.models.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {


}