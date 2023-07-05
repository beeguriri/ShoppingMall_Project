package study.shop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import study.shop.entity.constant.ItemSellStatus;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"itemName", "price", "stock"})
public class Item extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Lob //Large Object(var255 이상의 데이터 저장)
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    //==비지니스 로직 : 도메인 주도 설계==//
    //재고증가
    public void addStock(int quantity) {
        this.stock += quantity;
    }

    //재고감소
    public void removeStock(int quantity) {
        int resultStock = this.stock - quantity;

        //resultStock <0 일때, 예외처리 필요
        if(resultStock<0)
            throw new RuntimeException("재고부족");

        this.stock = resultStock;
    }


}
