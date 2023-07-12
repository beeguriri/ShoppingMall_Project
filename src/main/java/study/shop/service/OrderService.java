package study.shop.service;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import study.shop.dto.OrderDto;
import study.shop.dto.OrderHistoryDto;
import study.shop.dto.OrderItemDto;
import study.shop.entity.*;
import study.shop.repository.ItemImgRepository;
import study.shop.repository.ItemRepository;
import study.shop.repository.MemberRepository;
import study.shop.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String userid) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException(userid));

        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        //스프레드 연산자로 펼쳐놨는데.. 어떻게 받아올지 좀더 고민
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getOrderList(String userid, Pageable pageable){

        List<Order> orders = orderRepository.findOrders(userid, pageable);
        JPAQuery<Long> countQuery = orderRepository.countQuery(userid);

        List<OrderHistoryDto> orderHistoryList = new ArrayList<>();

        for(Order order : orders) {
            OrderHistoryDto orderHistoryDto = new OrderHistoryDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for(OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistoryDto.addOrderItemDto(orderItemDto);
            }

            orderHistoryList.add(orderHistoryDto);
        }

//        return new PageImpl<>(orderHistoryList, pageable, totalCount);
        return PageableExecutionUtils.getPage(orderHistoryList, pageable, countQuery::fetchOne);

    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String userid) {
        Member curMember = memberRepository.findByUserid(userid)
                .orElseThrow(
                        () -> new UsernameNotFoundException(userid)
                );

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        Member orderMember = order.getMember();

        return StringUtils.equals(curMember.getUserid(), orderMember.getUserid());
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancel();
    }

    public Long cartToOrder(List<OrderDto> orderDtoList, String userid) {

        Member member = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException(userid));

        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderDto orderDto : orderDtoList) {

            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
