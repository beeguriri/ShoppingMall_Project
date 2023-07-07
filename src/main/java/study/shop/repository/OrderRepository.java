package study.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shop.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
