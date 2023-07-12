package study.shop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<CartItem> cartItems = new ArrayList<>();

    //생성자 (단방향)
    public Cart(Member member) {
        this.member = member;
    }

    //양방향
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    //생성메서드
//    public static Cart createCart(CartItem... cartItems){
//
//        Cart cart = new Cart();
//
//        for(CartItem item : cartItems)
//            cart.addCartItem(item);
//
//        return cart;
//    }

    //비즈니스 로직
    public void cancel() {
        for(CartItem item : cartItems)
            item.cancel();
    }

    public int getTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }
}
