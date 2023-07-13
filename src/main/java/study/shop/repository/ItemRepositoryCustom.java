package study.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.shop.dto.ItemSearchDto;
import study.shop.dto.MainItemDto;
import study.shop.dto.ManageItemDto;

public interface ItemRepositoryCustom {

    Page<ManageItemDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
