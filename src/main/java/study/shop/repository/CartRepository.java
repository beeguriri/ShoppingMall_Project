package study.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
