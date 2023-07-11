package study.shop.dto;

import lombok.Data;
import study.shop.entity.Order;
import study.shop.entity.constant.OrderStatus;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderHistoryDto {

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus; // ORDER, CANCEL
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistoryDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }


}
