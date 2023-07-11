package study.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import study.shop.dto.OrderDto;
import study.shop.dto.OrderHistoryDto;
import study.shop.service.OrderService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public @ResponseBody ResponseEntity<?> Order (@RequestBody @Valid OrderDto orderDto,
                                                     BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError error : fieldErrors) {
                sb.append(error.getDefaultMessage());
                log.info("error={}", error);
            }

            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String userid = principal.getName();
        Long orderId;

        try {
            orderId = orderService.order(orderDto, userid);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHistory(@PathVariable("page") Optional<Integer> page,
                               Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.orElse(0),2);
        Page<OrderHistoryDto> orderHistoryList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHistoryList);
        model.addAttribute("page", pageable.getPageNumber()); //페이지 전환 시 기존 검색조건 유지
        model.addAttribute("maxPage", 5);

        return "orders/orderHistory";
    }
}
