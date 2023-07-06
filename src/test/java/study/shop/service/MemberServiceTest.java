package study.shop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.transaction.annotation.Transactional;
import study.shop.dto.MemberFormDto;
import study.shop.entity.Member;
import study.shop.entity.constant.Role;
import study.shop.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        MemberFormDto memberFormDto = createForm();
        memberService.createMember(memberFormDto);

        //when
        Member savedMember = memberRepository.findByUserid(memberFormDto.getUserid()).get();

        //then
        assertEquals(memberFormDto.getEmail(), savedMember.getEmail());
        assertEquals(memberFormDto.getNickName(), savedMember.getNickName());
        assertEquals(savedMember.getRole(), Role.USER);
    }

    @Test
    public void 중복회원가입() throws Exception {
        //given
        MemberFormDto memberForm1 = createForm();
        MemberFormDto memberForm2 = createForm();

        //when
        memberService.createMember(memberForm1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.createMember(memberForm2);
        });

        //then
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
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