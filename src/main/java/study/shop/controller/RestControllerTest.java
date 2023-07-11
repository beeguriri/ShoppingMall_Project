package study.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.shop.dto.ItemSearchDto;
import study.shop.dto.OrderHistoryDto;
import study.shop.entity.Item;
import study.shop.service.ItemService;
import study.shop.service.OrderService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RestControllerTest {

    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping(value = {"/admin/v2/items", "/admin/v2/items/{page}"})
    public Page<Item> adminItemPage(ItemSearchDto itemSearchDto,
                                @PathVariable("page") Optional<Integer> page) {

        Pageable pageable = PageRequest.of(page.orElse(0),3);

        return itemService.getAdminItemPage(itemSearchDto, pageable);
    }

    @GetMapping(value = {"/admin/v2/orders", "/admin/v2/orders/{page}"})
    public Page<OrderHistoryDto> orderHistoryList(@PathVariable("page") Optional<Integer> page,
                                                  Principal principal) {

        Pageable pageable = PageRequest.of(page.orElse(0),3);

        return orderService.getOrderList(principal.getName(), pageable);
    }
}
