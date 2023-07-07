package study.shop.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.shop.dto.MemberFormDto;
import study.shop.repository.CartRepository;
import study.shop.repository.MemberRepository;
import study.shop.service.MemberService;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 카트와멤버매핑() throws Exception {

        MemberFormDto memberForm = createForm();
        memberService.createMember(memberForm);
        Member member = memberRepository.findByUserid(memberForm.getUserid()).get();

        Cart cart = new Cart(member);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart findCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(findCart.getMember().getId(), member.getId());
    }

    public MemberFormDto createForm() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setUserid("tester1");
        memberFormDto.setNickName("테스터");
        memberFormDto.setEmail("aaa@bbb.com");
        memberFormDto.setPassword("1234");
        memberFormDto.setAddress("부산시");

        return memberFormDto;
    }
}