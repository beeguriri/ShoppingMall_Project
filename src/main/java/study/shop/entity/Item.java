package study.shop.entity;

import lombok.*;
import study.shop.entity.constant.ItemSellStatus;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@ToString(of = {"itemName", "price", "stock"})
public class Item extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false) @Setter
    private String itemName;

    @Column(nullable = false) @Setter
    private int price;

    @Column(nullable = false) @Setter
    private int stock;

    @Lob //Large Object(var255 이상의 데이터 저장)
    @Column(nullable = false) @Setter
    private String itemDetail;

    @Enumerated(EnumType.STRING) @Setter
    private ItemSellStatus itemSellStatus;

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
