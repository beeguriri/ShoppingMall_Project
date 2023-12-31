package study.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import study.shop.dto.CartDetailDto;
import study.shop.dto.CartItemDto;
import study.shop.dto.CartOrderDto;
import study.shop.dto.OrderDto;
import study.shop.entity.Cart;
import study.shop.entity.CartItem;
import study.shop.entity.Item;
import study.shop.entity.Member;
import study.shop.repository.CartItemRepository;
import study.shop.repository.CartRepository;
import study.shop.repository.ItemRepository;
import study.shop.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String userid){

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException(userid));

        Cart cart = cartRepository.findByMemberId(member.getId());

        if(cart==null) {
            cart = new Cart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem!=null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String userid) {

        Member member = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException(userid));

        Cart cart = cartRepository.findByMemberId(member.getId());

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        if(cart == null)
            return cartDetailDtoList;

        return cartItemRepository.findCartDetailDtoList(cart.getId());

    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String userid) {

        Member curMember = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException(userid));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        Member savedMember = cartItem.getCart().getMember();

        return StringUtils.equals(curMember.getUserid(), savedMember.getUserid());

    }

    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String userid) {

        List<OrderDto> orderDtoList = new ArrayList<>();

        for(CartOrderDto cartOrderDto : cartOrderDtoList) {

            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.cartToOrder(orderDtoList, userid);

        for(CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }

}
