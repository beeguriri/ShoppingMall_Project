package study.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.shop.dto.MemberFormDto;
import study.shop.dto.MemberUpdateFormDto;
import study.shop.entity.Member;
import study.shop.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member getMember(String userid) {
        return memberRepository.findByUserid(userid).orElseThrow(
                () -> new UsernameNotFoundException(userid)
        );
    }

    public void createMember(MemberFormDto memberFormDto){

        //이미 가입된 회원 검증
        memberRepository.findByUserid(memberFormDto.getUserid())
                .ifPresent(member -> {
                    throw new IllegalStateException("이미 가입된 아이디 입니다.");
                });

        memberRepository.findByEmail(memberFormDto.getEmail())
                .ifPresent(member -> {
                    throw new IllegalStateException("이미 가입된 이메일 입니다.");
                });

        //신규가입 진행
        Member newMember = Member.createMember(memberFormDto, passwordEncoder);
        memberRepository.save(newMember);
    }

    public void updateMember(MemberUpdateFormDto memberUpdateFormDto) {

        Member updateMember = memberRepository.findByUserid(memberUpdateFormDto.getUserid())
                .orElseThrow(
                        () -> new UsernameNotFoundException(memberUpdateFormDto.getUserid())
                );

//        memberRepository.findByEmail(memberUpdateFormDto.getEmail())
//                .ifPresent(member -> {
//                    throw new IllegalStateException("이미 가입된 이메일 입니다.");
//                });

        updateMember.setNickName(memberUpdateFormDto.getNickName());
        updateMember.setPassword(passwordEncoder.encode(memberUpdateFormDto.getPassword()));
        updateMember.setEmail(memberUpdateFormDto.getEmail());
        updateMember.setAddress(memberUpdateFormDto.getAddress());

    }

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserid(userid).orElseThrow(
                () -> new UsernameNotFoundException(userid)
        );

        return User.builder()
                .username(member.getUserid())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
