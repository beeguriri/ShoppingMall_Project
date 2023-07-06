package study.shop.dto;

import lombok.Data;
import study.shop.entity.constant.Role;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class MemberFormDto {

    private String userid;

    private String nickName;

    private String email;

    private String password;

    private String address;

}
