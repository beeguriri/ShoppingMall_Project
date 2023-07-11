package study.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.shop.dto.OrderDto;
import study.shop.entity.Item;
import study.shop.entity.Member;
import study.shop.entity.Order;
import study.shop.entity.OrderItem;
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
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String userid) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException(userid));

        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getOrderPrice(), orderDto.getCount());
        orderItemList.add(orderItem);

        //스프레드 연산자로 펼쳐놨는데.. 어떻게 받아올지 좀더 고민
        Order order = Order.createOrder(member, orderItem);
        orderRepository.save(order);

        return order.getId();
    }
}
