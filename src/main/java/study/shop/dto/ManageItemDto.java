package study.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import study.shop.entity.constant.ItemSellStatus;

import java.time.LocalDateTime;

@Data
public class ManageItemDto {

    private Long id;

    private String itemName;

    private ItemSellStatus itemSellStatus;

    private String createdBy;

    private LocalDateTime createdAt;

    @QueryProjection
    public ManageItemDto(Long id, String itemName, ItemSellStatus itemSellStatus, String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.itemName = itemName;
        this.itemSellStatus = itemSellStatus;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
}
