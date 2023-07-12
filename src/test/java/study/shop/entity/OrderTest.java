package study.shop.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.shop.dto.MemberFormDto;
import study.shop.entity.constant.ItemSellStatus;
import study.shop.repository.MemberRepository;
import study.shop.repository.OrderRepository;
import study.shop.service.MemberService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static study.shop.entity.Order.createOrder;
import static study.shop.entity.OrderItem.createOrderItem;

@SpringBootTest
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 주문생성() throws Exception {
        //given
        MemberFormDto memberForm = createForm();
        memberService.createMember(memberForm);
        Member member = memberRepository.findByUserid(memberForm.getUserid()).get();

        List<OrderItem> list = new ArrayList<>();
        OrderItem orderItem1 = createOrderItem(createItem("itemA", 1000, 10), 1000, 5);
        OrderItem orderItem2 = createOrderItem(createItem("itemB", 2000, 10), 1000, 1);
        list.add(orderItem1);
        list.add(orderItem2);

        //when
        Order order = createOrder(member, list);
        orderRepository.save(order);

        em.clear();

        //then
        assertEquals(order.getMember().getUserid(), "tester1");
        assertEquals(order.getOrderItems().size(), 2);
        assertEquals(order.getOrderItems().get(0).getItem().getStock(), 5);
        assertEquals(order.getOrderItems().get(1).getItem().getStock(), 9);

    }

    @Test
    public void 주문생성실패_재고부족() throws Exception {
        //given
        MemberFormDto memberForm = createForm();
        memberService.createMember(memberForm);
        Member member = memberRepository.findByUserid(memberForm.getUserid()).get();

        Item itemA = createItem("itemA", 1000, 10);
        OrderItem orderItem1 = createOrderItem(itemA, 1000, 5);
        assertThrows(RuntimeException.class,
                () -> createOrderItem(itemA, 1000, 6));

    }

    @Test
    public void 주문취소() throws Exception {
        //given
        MemberFormDto memberForm = createForm();
        memberService.createMember(memberForm);
        Member member = memberRepository.findByUserid(memberForm.getUserid()).get();

        Item itemA = createItem("itemA", 1000, 10);
        Item itemB = createItem("itemB", 2000, 10);
        List<OrderItem> list = new ArrayList<>();
        OrderItem orderItem1 = createOrderItem(itemA, 1000, 5);
        OrderItem orderItem2 = createOrderItem(itemB, 2000, 1);
        list.add(orderItem1);
        list.add(orderItem2);

        Order order = createOrder(member, list);
        orderRepository.save(order);

        em.clear();

        order.cancel();
        List<OrderItem> orderItemListlist = order.getOrderItems();
        for (OrderItem orderItem : orderItemListlist) {
            System.out.println("orderItem = " + orderItem.getItem());
        }

        assertEquals(order.getOrderItems().get(0).getItem().getStock(), 10);
        assertEquals(order.getOrderItems().get(1).getItem().getStock(), 10);

    }

    public MemberFormDto createForm() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setUserid("tester11");
        memberFormDto.setNickName("테스터");
        memberFormDto.setEmail("aaa@11bbb.com");
        memberFormDto.setPassword("1234");
        memberFormDto.setAddress("부산시");

        return memberFormDto;
    }

    private static Item createItem(String itemName, int price, int stock) {
        return new Item(itemName, price, stock, "상세설명", ItemSellStatus.SELL);
    }
}