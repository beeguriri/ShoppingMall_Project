package study.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.shop.dto.MemberFormDto;
import study.shop.service.MemberService;

import javax.validation.Valid;

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
            log.info("field={}, error={}",bindingResult.getFieldError().getField(), bindingResult.getFieldError());
            return "members/memberForm";
        }

        memberService.createMember(memberFormDto);

        return "redirect:/";
    }
}
