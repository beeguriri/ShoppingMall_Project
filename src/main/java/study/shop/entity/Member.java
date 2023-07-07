package study.shop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.shop.dto.MemberFormDto;
import study.shop.entity.constant.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String userid;

    @Column(nullable = false) @Setter
    private String nickName;

    @Column(unique = true, nullable = false) @Setter
    private String email;

    @Column(nullable = false) @Setter
    private String password;

    @Column(nullable = false) @Setter
    private String address;

    @Enumerated(EnumType.STRING) @Setter
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member(String userid, String nickName, String email, String password, String address, Role role) {
        this.userid = userid;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        return new Member(memberFormDto.getUserid(), memberFormDto.getNickName(), memberFormDto.getEmail(),
                passwordEncoder.encode(memberFormDto.getPassword()), memberFormDto.getAddress(), Role.ADMIN);
    }
}
