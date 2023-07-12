package study.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import study.shop.dto.CartDetailDto;
import study.shop.dto.CartItemDto;
import study.shop.dto.CartOrderDto;
import study.shop.service.CartService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public @ResponseBody ResponseEntity<?> order(@RequestBody @Valid CartItemDto cartItemDto,
                                                 BindingResult bindingResult, Principal principal){

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
        Long cartItemId;

        try{
            cartItemId = cartService.addCart(cartItemDto, userid);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public String cartList(Principal principal, Model model) {

        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());

        model.addAttribute("cartItems", cartDetailList);

        return "cart/cartList";
    }

    @PatchMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity<?> updateCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                          int count, Principal principal) {
        if(count<=0)
            return new ResponseEntity<>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        else if(!cartService.validateCartItem(cartItemId, principal.getName()))
            return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);

        cartService.updateCartItemCount(cartItemId, count);

        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity<?> deleteCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                          Principal principal){

        if(!cartService.validateCartItem(cartItemId, principal.getName()))
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);

        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    @PostMapping("/cart/orders")
    public @ResponseBody ResponseEntity<?> orderCartItem(@RequestBody CartOrderDto cartOrderDto,
                                                         Principal principal) {

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        if(cartOrderDtoList == null || cartOrderDtoList.size()==0)
            return new ResponseEntity<>("주문 할 상품을 선택해 주세요.", HttpStatus.FORBIDDEN);

        for(CartOrderDto cartOrder : cartOrderDtoList)
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName()))
                return new ResponseEntity<>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);

        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());

        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}
