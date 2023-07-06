package study.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.shop.dto.MemberFormDto;
import study.shop.dto.MemberUpdateFormDto;
import study.shop.entity.Member;
import study.shop.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "members/memberForm";
    }

    @PostMapping("/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        // 입력값에 오류 있으면 bindingResult 에 담아서 form 으로 이동
        if(bindingResult.hasErrors()) {
            log.info("error={}", bindingResult.getFieldError());
            return "members/memberForm";
        }

        // 중복 회원가입 검증
        try {
            memberService.createMember(memberFormDto);
        } catch (IllegalStateException e) {
            log.info("error={}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "members/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "members/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginError", "아이디 또는 비밀번호를 확인해주세요.");
        return "members/memberLoginForm";
    }

    @GetMapping("/update")
    public String update(Principal principal, Model model) {
        log.info("로그인 된 사용자 id = {}", principal.getName());
        Member updateMember = memberService.getMember(principal.getName());
        model.addAttribute("updateMember", updateMember);

        return "members/memberUpdateForm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("updateMember") @Valid MemberUpdateFormDto memberUpdateFormDto, BindingResult bindingResult, Model model, Principal principal) {


//        // 입력값에 오류 있으면 bindingResult 에 담아서 form 으로 이동
        if(bindingResult.hasErrors()) {
            log.info("error={}", bindingResult.getFieldError());
            return "members/memberUpdateForm";
        }
//
//        try {
            memberService.updateMember(memberUpdateFormDto);
//        } catch (IllegalStateException e) {
//            log.info("error={}", e.getMessage());
//            model.addAttribute("errorMessage", e.getMessage());
//            return "members/memberUpdateForm";
//        }
        return "redirect:/";
    }
}
