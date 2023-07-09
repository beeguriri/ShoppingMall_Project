package study.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shop.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
}
