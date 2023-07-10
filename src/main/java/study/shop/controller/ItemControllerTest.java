package study.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.shop.dto.ItemSearchDto;
import study.shop.entity.Item;
import study.shop.service.ItemService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ItemControllerTest {

    private final ItemService itemService;

    @GetMapping(value = {"/admin/v2/items", "/admin/v2/items/{page}"})
    public Page<Item> adminItemPage(ItemSearchDto itemSearchDto,
                                @PathVariable("page") Optional<Integer> page) {

        Pageable pageable = PageRequest.of(page.orElse(0),3);

        return itemService.getAdminItemPage(itemSearchDto, pageable);
    }
}
