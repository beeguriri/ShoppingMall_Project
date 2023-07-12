package study.shop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @Setter
    private Cart cart;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @Setter
    private Item item;

    @Setter
    private int count;

    //생성메서드
    public static CartItem createCartItem(Cart cart, Item item, int count){

        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setCount(count);
        cart.addCartItem(cartItem);

//        item.removeStock(count);
        return cartItem;

    }

    //비즈니스 로직
    public void cancel() {
        getItem().addStock(count);
    }

    //조회 로직
    public int getTotalPrice() {
        return getCount() * getItem().getPrice();
    }

    //기존 상품에 count 추가 로직
    public void addCount(int count){
        this.count += count;
    }
}
