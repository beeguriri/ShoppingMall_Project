package study.shop.dto;

import lombok.Data;
import study.shop.entity.OrderItem;

@Data
public class OrderItemDto {

    private String itemName;
    private int count;
    private int orderPrice;
    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getItem().getPrice();
        this.imgUrl = imgUrl;
    }
}
