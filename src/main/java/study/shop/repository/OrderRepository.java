package study.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

}
