package study.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.shop.entity.Item;
import study.shop.entity.ItemImg;
import study.shop.entity.constant.ItemSellStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName;

    @NotNull(message = "가격은 필수 입력값입니다.")
    private int price;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private int stock;

    @NotBlank(message = "상품설명은 필수 입력 값입니다.")
    private String itemDetail;

    private ItemSellStatus itemSellStatus;

    //상품 저장 후 수정할 때 상품 이미지 정보 저장
    private List<ItemImg> itemImgsList = new ArrayList<>();

    // 상품의 이미지 아이디 저장
    private List<Long> itemImgIds = new ArrayList<>();

    // 생성자
    public ItemFormDto(Long id, String itemName, int price, int stock,
                       String itemDetail, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stock = stock;
        this.itemSellStatus = itemSellStatus;
    }

    //itemFormDto 와 item 간 데이터 복사
    public Item createItem() {
        return new Item(itemName, price, stock, itemDetail, itemSellStatus);
    }

    public ItemFormDto of(Item item){
        return new ItemFormDto(
                item.getId(),
                item.getItemName(),
                item.getPrice(),
                item.getStock(),
                item.getItemDetail(),
                item.getItemSellStatus()
        );
    }

}
