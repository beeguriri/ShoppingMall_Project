package study.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.shop.dto.ItemSearchDto;
import study.shop.entity.Item;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
