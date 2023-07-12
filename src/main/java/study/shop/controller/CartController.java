package study.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import study.shop.dto.CartDetailDto;
import study.shop.dto.CartItemDto;
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
}
