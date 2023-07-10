package study.shop.entity;

import lombok.*;
import study.shop.dto.ItemFormDto;
import study.shop.entity.constant.ItemSellStatus;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"itemName", "price", "stock"})
public class Item extends BaseEntity{

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

    //==아이템 생성==//
    public Item(String itemName, int price, int stock, String itemDetail, ItemSellStatus itemSellStatus) {
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
    }

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stock = itemFormDto.getStock();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if (!(obj instanceof Item that))
            return false;
        return id!=null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

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
