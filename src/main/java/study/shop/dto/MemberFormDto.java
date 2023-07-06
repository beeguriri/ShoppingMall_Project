package study.shop.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class MemberFormDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userid;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String nickName;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
//    @Length(min = 8, max = 16, message = "비밀번호는 8자이상, 16자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

}
