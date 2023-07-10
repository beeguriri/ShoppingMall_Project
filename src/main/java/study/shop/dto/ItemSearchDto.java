package study.shop.dto;

import lombok.Data;
import study.shop.entity.constant.ItemSellStatus;

@Data
public class ItemSearchDto {

    private String searchDateType; //all, 1d, 1w, 1m, 6m

    private ItemSellStatus searchSellStatus;

    private String searchBy; //itemName, createdBy

    private String searchQuery = "";

}
