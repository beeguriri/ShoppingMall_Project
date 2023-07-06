package study.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.shop.dto.MemberFormDto;
import study.shop.entity.Member;
import study.shop.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void createMember(MemberFormDto memberFormDto){

        //이미 가입된 회원 검증
        memberRepository.findByUserid(memberFormDto.getUserid())
                .ifPresent(member -> {
                    throw new IllegalStateException("이미 가입된 회원입니다.");
                });

        //신규가입 진행
        Member newMember = Member.createMember(memberFormDto, passwordEncoder);
        memberRepository.save(newMember);
    }
}
