package study.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shop.entity.ItemComment;

import java.util.List;

public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {

    List<ItemComment> findByItemIdOrderByUpdatedAtDesc(Long itemId);
}
