package study.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @NotBlank(message = "상품설명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private int stock;

    private ItemSellStatus itemSellStatus;

    //상품 저장 후 수정할 때 상품 이미지 정보 저장
    private List<ItemImg> itemImgsList = new ArrayList<>();

    // 상품의 이미지 아이디 저장
    private List<Long> itemImgIds = new ArrayList<>();

}
