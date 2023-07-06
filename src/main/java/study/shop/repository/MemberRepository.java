package study.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shop.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserid(String userid);

    Optional<Member> findByEmail(String email);
}
