package study.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
