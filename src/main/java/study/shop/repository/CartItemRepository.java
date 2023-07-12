package study.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
}
